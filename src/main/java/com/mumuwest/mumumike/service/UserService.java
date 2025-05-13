package com.mumuwest.mumumike.service;

import com.mumuwest.mumumike.pojo.User;

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
}
