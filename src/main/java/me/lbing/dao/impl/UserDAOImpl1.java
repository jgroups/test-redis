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
 * 该类么有验证
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

        boolean result = (boolean) redisTemplate.execute(new RedisCallback() {

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

    /**
     * 添加一个User的List对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(List<User> list) {

        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        return (boolean) redisTemplate.execute(new RedisCallback() {

                                                   @SuppressWarnings("rawtypes")
                                                   @Override
                                                   public Boolean doInRedis(RedisConnection connection)
                                                           throws DataAccessException {

                                                       RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                                                       RedisSerializer<User> valueSerializer = redisTemplate.getValueSerializer();
                                                       for (User user : list) {
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

    @Override
    public void delete(String key) {
        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                return redisConnection.del(keySerializer.serialize(key));
            }
        });
    }

    @Override
    public void delete(List<String> keys) {
        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                for (String key : keys) {
                    redisConnection.del(keySerializer.serialize(key));
                }
                return null;
            }
        });
    }

    @Override
    public boolean update(User user) throws Exception {
        return false;
    }

    @Override
    public User get(String keyId) {
        final RedisTemplate redisTemplate = redisTemplateFactory.getLocalRedisTemplate();

        return (User)redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                return redisConnection.get(keySerializer.serialize(keyId));
            }
        });
    }

    @Override
    public boolean opsForList(String key, User user) {
        return false;
    }

    @Override
    public boolean getListValueByIndex(String key, int index) {
        return false;
    }
}
