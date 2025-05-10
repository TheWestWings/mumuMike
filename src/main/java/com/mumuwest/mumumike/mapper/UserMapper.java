package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectUserByUsername(String username);

    // 根据邮箱查询用户
    User selectUserByEmail(String email);

    // 根据手机号查询用户
    User selectUserByPhone(String phone);

    // 插入新用户
    int insertUser(User user);

    // 更新用户信息
    int updateUser(User user);

    // 删除用户
    int deleteUser(int id);
}
