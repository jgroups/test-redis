package me.lbing;

import me.lbing.dao.UserDAO;
import me.lbing.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-redis.xml" })
public class SpringTestCase {

    @Autowired
    private UserDAO userDAO;

    private static final Integer COUNTER = 10;

    // 测试添加单个User
    @Test
    public void testAddUser() {
        for (int i = 1; i <= COUNTER; i++) {
            User user = new User("mykey000" + i, "zjroger000" + i, "password000" + i);
            if (userDAO.add(user)) {
                System.out.println("用户zjrodger" + i + "添加成功。");
            } else {
                System.out.println("用户zjrodger" + i + "添加失败。");
            }
        }
    }

    // 测试按照Key-Value方式添加User
    @Test
    public void testAddUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= COUNTER; i++) {
            User user = new User("mykey000" + i, "zjroger000" + i, "password000" + i);
            userList.add(user);
        }
        if (userDAO.add(userList)) {
            System.out.println("用户列表添加成功。");
        } else {
            System.out.println("用户列表添加成功。");
        }
    }

    // 通过OpsForList方式添加User
    @Test
    public void testOpsForList() {
        String myListKey = "key:myListKey01";
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= COUNTER; i++) {
            User user = new User("myUserList0" + i, "myUserList0" + i, "myUserList0" + i);
            userDAO.opsForList(myListKey, user);
        }
    }

    @Test
    public void testgetListValueByIndex() {
        String myListKey = "key:myListKey01";
        int index = 0;
        userDAO.getListValueByIndex(myListKey, index);
    }

    // 测试删除User
    @Test
    public void testDelete() {
        // 单个删除
//        String key = "mykey0002";
//        userDao.delete(key);

        // 多个删除
        List<String> keys = new ArrayList<>();
        keys.add("mykey0001");
        keys.add("mykey0002");
        keys.add("mykey0003");
        userDAO.delete(keys);
    }

    // 测试获取User
    @Test
    public void testGetUser() {
        User user = userDAO.get("mykey0001");
        System.out.println(user.toString());
        // 多条查询
//        for(int i=1; i<=COUNTER; i++){
//            User user = userDao.get("mykey0"+i);
//            System.out.println(user.toString());
//        }
    }

    // 测试更新User
    @Test
    public void testUpdateUser() {
        String mykey01 = "mykey0001";
        User user = userDAO.get(mykey01);
        System.out.println("原来的用户：" + user.toString());
        user.setUsername("ttttt");

        try {
            boolean result = userDAO.update(user);
            System.out.println("数据更新完毕。");
            System.out.println(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}