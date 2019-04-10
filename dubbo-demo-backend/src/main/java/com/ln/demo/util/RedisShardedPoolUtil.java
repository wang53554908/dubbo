package com.ln.demo.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by chuck
 */
public class RedisShardedPoolUtil {

    /**
     * 设置key 的有效期,单位是秒
     *
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(JedisPool jedisPool,String key, int exTime) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
//            log.error("expire key:{} error", key, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }

    //exTime的单位是秒
    public static String setEx(JedisPool jedisPool,String key, String value, int exTime) {
    	Jedis jedis = null;
        String result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
//            log.error("setex key:{} value:{} error", key, value, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }


    public static String set(JedisPool jedisPool,String key, String value) {
    	Jedis jedis = null;
        String result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
//            log.error("set key:{} value:{} error", key, value, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }



    public static String getSet(JedisPool jedisPool,String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
//            log.error("getSet key:{} value:{} error", key, value, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }

    public static String get(JedisPool jedisPool,String key) {
    	Jedis jedis = null;
        String result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
//            log.error("set key:{} error", key, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }


    public static Long del(JedisPool jedisPool,String key) {
        Jedis jedis = null;
        Long result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
//            log.error("del key:{} error", key, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }


    public static Long setnx(JedisPool jedisPool,String key, String value) {
        Jedis jedis = null;
        Long result = null;

        try {
        	jedis = jedisPool.getResource();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
//            log.error("setnx key:{} value:{} error", key, value, e);
        	jedis.close();
            return result;
        }
        jedis.close();
        return result;
    }
}
