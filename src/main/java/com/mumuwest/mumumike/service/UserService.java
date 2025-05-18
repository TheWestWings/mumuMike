package com.mumuwest.mumumike.service;

import com.mumuwest.mumumike.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @param phone    手机号
     * @return 注册结果
     */
    Integer register(String username, String password, String email, String phone);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    String login(String username, String password);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(Integer userId);


    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer updateUserInfo(User user);

    /**
     * 获取所有用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    List<User> getList(User user);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User getUserById(Integer id);
}
