package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.UserMapper;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @param phone    手机号
     * @return
     */
    public Integer register(String username, String password, String email, String phone) {
        return userMapper.insertUser(new User(username, password, email, phone));
    }

    @Override
    public String login(String username, String password) {
        return "";
    }

    public User getUserInfo(Integer userId) {
        // 实现获取用户信息逻辑
        return new User();
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public Integer updateUserInfo(User user) {
        // 实现更新用户信息逻辑
        return userMapper.updateUserInfo(user);
    }

    @Override
    public List<User> getList(User user) {
        // 实现获取用户列表逻辑
        return userMapper.selectAllUsers();
    }
}
