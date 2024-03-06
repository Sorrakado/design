package com.kyou.net.design.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCommonProcessor {

    @Resource
    private RedisTemplate redisTemplate;

    public Object get(String key){
        if(key == null){
            throw new UnsupportedOperationException("Redis Key cannot be  null!");
        }
        return redisTemplate.opsForValue().get(key);
    }
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }
    public void set(String key, Object value,long timeout){
        if(timeout > 0){
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        }else{
            set(key,value);
        }
    }
}
