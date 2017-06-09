package me.lbing.dao.impl;

import me.lbing.RedisTemplateFactory;
import me.lbing.dao.UserDAO;
import me.lbing.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component( value = "userDAO")
public class UserDAOImpl implements UserDAO {

    @Autowired
    public RedisTemplateFactory redisTemplateFactory;

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(User user) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        opsForValue.set(user.getUserId(), user);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(List<User> list) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        for(User user: list){
            opsForValue.set(user.getUserId(), user);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(String key) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        opsForValue.getOperations().delete(key);
    }

    @SuppressWarnings("unused")
    @Override
    public void delete(List<String> keys) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        RedisOperations<String, Object> operations = opsForValue.getOperations();
        for(String key : keys){
            operations.delete(key);
        }
    }

    @Override
    public boolean update(User user) throws Exception {
        if( null == user.getUserId()){
            throw new Exception("该用户的Key不存在");
        }
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        opsForValue.set(user.getUserId(), user);
        return true;
    }

    @Override
    public User get(String keyId) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ValueOperations<String, Object> opsForValue = localRedisTemplate.opsForValue();
        return (User)opsForValue.get(keyId);
    }

    @Override
    public boolean opsForList(String key, User user) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ListOperations<String, Object> opsForList = localRedisTemplate.opsForList();
        opsForList.rightPush(key, user);
        return true;
    }

    @Override
    public boolean getListValueByIndex(String key, int index) {
        RedisTemplate<String, Object> localRedisTemplate = redisTemplateFactory.getLocalRedisTemplate();
        ListOperations<String, Object> opsForList = localRedisTemplate.opsForList();
        User user = (User) opsForList.index(key, index);
        System.out.println(user);
        return true;
    }
}
