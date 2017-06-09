package me.lbing.dao.impl;

import me.lbing.RedisTemplateFactory;
import me.lbing.dao.UserDAO;
import me.lbing.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * Created by King on 2017/6/6.
 */
public class UserDAOImpl1 implements UserDAO {

    @Autowired
    private RedisTemplateFactory redisTemplateFactory;

    /**
     * 添加一个User对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(User user) {

        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        boolean result = (boolean) redisTemplate.execute(new RedisCallback(){

            @SuppressWarnings("rawtypes")
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
                RedisSerializer<User> valueSerializer = redisTemplate.getValueSerializer();
                byte[] key = stringSerializer.serialize(user.getUserId());
                byte[] value = valueSerializer.serialize(user);
                return connection.setNX(key, value);
            }
        });
        return result;
    }

    /** 添加一个User的List对象*/
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(List<User> list) {

        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        return (boolean) redisTemplate.execute(new RedisCallback(){

                                                   @SuppressWarnings("rawtypes")
                                                   @Override
                                                   public Boolean doInRedis(RedisConnection connection)
                                                           throws DataAccessException {

                                                       RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                                                       RedisSerializer<User> valueSerializer = redisTemplate.getValueSerializer();
                                                       for(User user : list){
                                                           byte[] key = keySerializer.serialize(user.getUserId());
                                                           byte[] value = valueSerializer.serialize(user);
                                                           connection.setNX(key, value);
                                                       }
                                                       return true;
                                                   }
                                               },
                false,
                true);
    }
}
