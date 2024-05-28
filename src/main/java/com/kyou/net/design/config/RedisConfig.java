package com.kyou.net.design.config;


import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfig {

    @Resource
    RedisConnectionFactory  redisConnectionFactory;

    @Bean("redisTemplate")
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.setSerializerConfig(redisTemplate);
        return redisTemplate;
    }

    private void setSerializerConfig(RedisTemplate<String, Object> redisTemplate){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //普通key和hashkey
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //解决缓存转换异常的问题
        Jackson2JsonRedisSerializer<Object> redisValueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(redisValueSerializer);
        redisTemplate.setHashValueSerializer(redisValueSerializer);
        redisTemplate.afterPropertiesSet();
    }
}
