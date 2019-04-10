package com.ln.demo.shiro;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

import com.ln.demo.util.JwtUtils;
import com.ln.demo.util.RedisClient;
import com.ln.demo.util.RedisShardedPoolUtil;
import com.ln.demo.util.SecurityConsts;

public class JwtAuthcFilter extends AccessControlFilter {
	
	@Autowired
    private JwtUtils jwtUtils;
	
	@Autowired
    private JedisPool jedisPool;//注入JedisPool
	
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        //处理跨域请求
        if(request instanceof ShiroHttpServletRequest) {
            if(StringUtils.equalsIgnoreCase("OPTIONS", ((ShiroHttpServletRequest) request).getMethod())) {
                return true;
            }
        }
        
        // 拦截后先进入该方法。直接返回false，交由onAccessDenied处理鉴权与登录逻辑
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //从header中得到token
        String token = ((HttpServletRequest)request).getHeader("X-Token");
        String host = request.getRemoteHost();
		JwtToken jwtToken = new JwtToken(token, host);
		try {
			// 委托给Realm进行登录
			getSubject(request, response).login(jwtToken);
		} catch (Exception e) {
			if (e.getMessage().indexOf("令牌过期")!=-1) {
//				Long setnxResult = RedisShardedPoolUtil.setnx(jedisPool, token,
//						String.valueOf(System.currentTimeMillis() + 5000));
//				if (setnxResult != null && setnxResult.intValue() == 1) {
//					RedisShardedPoolUtil.expire(jedisPool, token, 5);
//					// AccessToken已过期
//					suc = this.refreshToken(request, response, token, host);
//					RedisShardedPoolUtil.del(jedisPool, token);
//				} else {
//					suc = true;
//					System.err.println("没有获取到分布式锁:" + token);
//				}
				((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
			}
			e.printStackTrace();
			return true;
		} 

		return true;
    }
    
    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response, String token, String host) {
    	// 设置RefreshToken中的时间戳为当前最新时间戳
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        // 获取当前Token的帐号信息
        String account = jwtUtils.getClaim(token, SecurityConsts.ACCOUNT);
        String refreshTokenCacheKey = SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
        String newToken = null;
        // 判断Redis中RefreshToken是否存在
        if (RedisClient.isExists(jedisPool, refreshTokenCacheKey)) {
        	 System.err.println(currentTimeMillis+"------"+"refreshTokenCacheKey:"+RedisClient.getRedis(jedisPool, refreshTokenCacheKey));
            // 获取RefreshToken时间戳,及AccessToken中的时间戳
            // 相比如果一致，进行AccessToken刷新
            String currentTimeMillisRedis = RedisClient.getRedis(jedisPool,refreshTokenCacheKey);
            String tokenMillis=jwtUtils.getClaim(token, SecurityConsts.CURRENT_TIME_MILLIS);
            System.err.println(currentTimeMillis+"------"+"newToken:"+token);
            System.err.println(currentTimeMillis+"------"+refreshTokenCacheKey);
            System.err.println(currentTimeMillis+"------"+"currentTimeMillisRedis:"+currentTimeMillisRedis+",tokenMillis:"+tokenMillis);

            // 刷新AccessToken，为当前最新时间戳
            newToken = jwtUtils.createJWT(account, currentTimeMillis);
            
            System.err.println(currentTimeMillis+"------"+"newToken:"+newToken);
            
            RedisClient.saveRedis(jedisPool,refreshTokenCacheKey, newToken, jwtUtils.getRefreshTokenExpireTime()*2);
            
            // 使用AccessToken 再次提交给ShiroRealm进行认证，如果没有抛出异常则登入成功，返回true
            JwtToken jwtToken = new JwtToken(newToken, host);
            this.getSubject(request, response).login(jwtToken);
 
                // 设置响应的Header头新Token
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, newToken);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);
            return true;
        }
        return false;
    }

}
