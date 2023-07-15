package com.csubigdata.futurestradingsystem.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //存缓存
    public void set(String key, String value, Long timeout){
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    //取缓存
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
    //清除缓存
    public void del(String key){
        stringRedisTemplate.delete(key);
    }
}
