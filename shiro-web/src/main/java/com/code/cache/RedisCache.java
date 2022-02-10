package com.code.cache;

import com.code.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @author fxs
 * @Date 2022/2/10 11:27
 * @Description:
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {
    
    @Resource
    private JedisUtil jedisUtil;
    
    /**
     * cache前缀
     */
    private final String CACHE_PREFIX = "cache_prefix";
    
    /**
     * 获取key
     * @param k
     * @return
     */
    private byte[] getKey(K k) {
        if(k instanceof String) {
            return (CACHE_PREFIX + k).getBytes();
        }
        //取序列化之后的数组
        return SerializationUtils.serialize(k);
    }
    
    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis中获取数据...");
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null) {
            //反序列化为对象
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }
    
    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println("从redis中存放数据...");
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);
        return v;
    }
    
    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }
    
    /**
     * clear方法不要重写
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {
    
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public Set<K> keys() {
        return null;
    }
    
    @Override
    public Collection<V> values() {
        return null;
    }
}
