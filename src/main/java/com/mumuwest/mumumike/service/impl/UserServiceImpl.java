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
     * 
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @param phone    手机号
     * @return
     */
    public Integer register(String username, String password, String email, String phone, Integer role) {
        User user = new User(username, password, email, phone);
        user.setRole(role);
        return userMapper.insertUser(user);
    }

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public String login(String username, String password) {
        return "";
    }

    /**
     * 获取用户信息
     * 
     * @param userId 用户ID
     * @return
     */
    public User getUserInfo(Integer userId) {
        // 实现获取用户信息逻辑
        return new User();
    }

    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    /**
     * 修改用户信息
     * 
     * @param user
     * @return
     */
    @Override
    public Integer updateUserInfo(User user) {
        // 实现更新用户信息逻辑
        return userMapper.updateUserInfo(user);
    }

    /**
     * 获取所有用户信息
     * 
     * @param user 用户信息
     * @return
     */
    @Override
    public List<User> getList(User user) {
        // 实现获取用户列表逻辑
        return userMapper.selectAllUsers(user);
    }

    /**
     * 根据id查询用户
     * 
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    /**
     * 根据openid查询用户（微信小程序登录）
     * 
     * @param openid 微信openid
     * @return 用户信息
     */
    @Override
    public User getUserByOpenid(String openid) {
        // 暂时使用用户名前缀匹配的方式查找微信用户
        // 实际项目中应该在User表中添加openid字段
        return userMapper.selectUserByUsername("wx_" + openid.substring(0, 8));
    }

}
