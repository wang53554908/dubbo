package com.ln.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.ln.demo.api.system.dto.RouterDTO;
import com.ln.demo.api.system.dto.UserDTO;
import com.ln.demo.api.system.service.RouterService;
import com.ln.demo.api.system.service.UserService;
import com.ln.demo.util.JwtUtils;
import com.ln.demo.util.RedisClient;
import com.ln.demo.util.SecurityConsts;
import com.ln.demo.vo.RouterNavVO;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RouterService routerService;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private JedisPool jedisPool;//注入JedisPool
    
    @PostMapping("/token")
    public ResponseEntity<?> getToken(HttpServletRequest request, HttpServletResponse resp,String loginName, String password) {
        // 验证用户信息
        UserDTO user = userService.getByLoginName(loginName);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名不存在！");
        }
        if(user.getLocked()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("该账户被锁定！");
        }
        Md5Hash md5 = new Md5Hash(password, user.getSalt(), 6);
        String md5Password = md5.toHex();
        if(!md5Password.equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败，用户名或密码错误！");
        }
        
        // 得到可访问的router
        List<RouterDTO> dtoList = routerService.listAuthorizedRouter(user.getId());
        List<RouterNavVO> voList = new ArrayList<RouterNavVO>(dtoList.size());
        for(RouterDTO dto : dtoList) {
            RouterNavVO vo = new RouterNavVO();
            BeanUtils.copyProperties(dto, vo);
            voList.add(vo);
        }
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("userId", user.getId().toString());
        response.put("username", user.getName());
        response.put("routers", voList);
        response.put("photoUrl", user.getPhotoUrl());
        JSONObject json = new JSONObject();
        json.put("userId", user.getId());
        json.put("username", user.getName());
        json.put("host", request.getRemoteHost());
        String token = jwtUtils.createJWT(json.toJSONString(),currentTimeMillis);
        response.put("token", token);
        //更新RefreshToken缓存的时间戳
//        String refreshTokenKey= SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + json.toJSONString();
//        if (RedisClient.isExists(jedisPool, refreshTokenKey)) {
//        	RedisClient.saveRedis(jedisPool, refreshTokenKey, token, jwtUtils.getRefreshTokenExpireTime()*2);
//        }else{
//        	RedisClient.saveRedis(jedisPool, refreshTokenKey, token, jwtUtils.getRefreshTokenExpireTime()*2);
//        }
        //写入header
//        resp.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, token);
//        resp.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);
        return ResponseEntity.ok(response);
    }
    
}
