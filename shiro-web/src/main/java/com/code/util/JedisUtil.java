package com.code.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author fxs
 * @Date 2022/2/8 15:25
 * @Description:
 */
@Component
public class JedisUtil {
    
    /**
     * jedis连接池
     */
    @Resource
    private JedisPool jedisPool;
    
    /**
     * 获取连接资源
     * @return
     */
    private Jedis getResource() {
        return jedisPool.getResource();
    }
    
    /**
     * 设置值
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }
    
    /**
     * 设置过期时间
     * @param key
     * @param i
     */
    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
    
    /**
     * 获取值
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }
    
    /**
     * 删除
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
    
    /**
     * 获取符合条件keys
     * @param shiro_session_prefix
     * @return
     */
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }
}
