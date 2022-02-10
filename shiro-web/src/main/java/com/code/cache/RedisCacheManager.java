package com.code.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * @author fxs
 * @Date 2022/2/10 11:27
 * @Description:
 */
public class RedisCacheManager implements CacheManager {
    
    @Resource
    private RedisCache redisCache;
    
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}
