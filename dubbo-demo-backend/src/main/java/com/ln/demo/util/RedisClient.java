package com.ln.demo.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

	public static void saveRedis(JedisPool jedisPool,String key, String value, Long expireSecond) {
		Jedis jedis = jedisPool.getResource();
	    boolean keyExist = jedis.exists(key);
	    // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
	    if (keyExist) {
	    	jedis.del(key);
	    }
	    jedis.set(key, value, "NX", "PX", expireSecond);
	    jedis.close();
	}
	
	public static String getRedis(JedisPool jedisPool,String key) {
		Jedis jedis = jedisPool.getResource();
	    String result = jedis.get(key);
	    jedis.close();
	    return result;
	}
	
	public static boolean isExists(JedisPool jedisPool,String key){
		Jedis jedis = jedisPool.getResource();
	    boolean result = jedis.exists(key);
	    jedis.close();
	    return result;
	}
	
}
