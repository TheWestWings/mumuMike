package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.UserMapper;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 示例方法
    public Integer register(String username, String password, String email, String phone) {

        return userMapper.insertUser(new User(username, password, email, phone));
    }

    public String login(String username, String password) {
        // 实现登录逻辑
        return "登录成功";
    }

    public User getUserInfo(Integer userId) {
        // 实现获取用户信息逻辑
        return new User();
    }
}
