package me.lbing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component( value ="redisTemplateFactory" )
public class RedisTemplateFactory {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("rawtypes")
    public RedisTemplate getLocalRedisTemplate(){
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        return this.redisTemplate;
    }
}
