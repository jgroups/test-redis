package me.lbing.dao;

import me.lbing.pojo.User;

import java.util.List;


public interface UserDAO {
    /** 新增 */
    boolean add(User user);

    /** 批量新增 使用pipeline方式 */
    boolean add(List<User> list);

    /**删除*/
    void delete(String key);

    /**删除多个*/
    void delete(List<String> keys);

    /**修改*/
    boolean update(User user) throws Exception;

    /**通过key获取*/
    User get(String keyId);

    boolean opsForList(String key, User user);

    boolean getListValueByIndex(String key, int index);
}
