package com.ln.demo.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt工具类
 * 
 * @author Lining
 * @date 2017/11/2
 */
public class JwtUtils {

	private String id;
	
	private String secret;
	
	/**
	 * 存活时间（毫秒）
	 */
	private long ttlMillis;
	
	/**
	 * 刷新token过期时间（毫秒）
	 */
	private long refreshTokenExpireTime;

	public JwtUtils(String id, String secret, long ttlMillis, long refreshTokenExpireTime) {
		this.id = id;
		this.secret = secret;
		this.ttlMillis = ttlMillis;
		this.refreshTokenExpireTime = refreshTokenExpireTime;
	}

	/**
	 * 生成加密key
	 * 
	 * @return
	 */
	private SecretKey generalKey() {
		byte[] encodedKey = Base64.getDecoder().decode(secret);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}

	/**
	 * 创建jwt
	 * 
	 * @param subject
	 * @return
	 */
	public String createJWT(String subject,String currentTimeMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey key = generalKey();
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).signWith(signatureAlgorithm,
				key);
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		builder.claim(SecurityConsts.CURRENT_TIME_MILLIS, currentTimeMillis)
		       .claim(SecurityConsts.ACCOUNT, subject);
		return builder.compact();
	}

	/**
	 * 解密jwt
	 * 
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public Claims parseJWT(String jwt) {
		SecretKey key = generalKey();
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		return claims;
	}
	
	 /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     * @return
     */
    public String getClaim(String token, String claim) {
        try {
        	DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    
    public String getAccountFromToken(String token,String key){
    	String account = this.getClaim(token, SecurityConsts.ACCOUNT);
    	JSONObject json = JSONObject.parseObject(account);
        if(json != null && json.containsKey(key)){
        	return json.getString(key);
        }	
        return null;
    }
    
    public long getRefreshTokenExpireTime(){
    	return refreshTokenExpireTime;
    }
    
    public static void main(String[] args) {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE1NTA2NTIzNzYsInN1YiI6IntcImhvc3RcIjpcIjEyNy4wLjAuMVwiLFwidXNlcklkXCI6MSxcInVzZXJuYW1lXCI6XCLns7vnu5_nrqHnkIblkZgxXCJ9IiwiZXhwIjoxNTUwNjUyMzg2LCJjdXJyZW50VGltZU1pbGxpcyI6IjE1NTA2NTIzNzY3MDgiLCJhY2NvdW50Ijoie1wiaG9zdFwiOlwiMTI3LjAuMC4xXCIsXCJ1c2VySWRcIjoxLFwidXNlcm5hbWVcIjpcIuezu-e7n-euoeeQhuWRmDFcIn0ifQ.4EPsnx9slVfFFlghpI8uJBFW-2yHVuOnniTsaFvI6fI";
	    System.err.println(new JwtUtils("jwt","12313",123,123).getClaim(token, SecurityConsts.ACCOUNT));
    }
   
}
