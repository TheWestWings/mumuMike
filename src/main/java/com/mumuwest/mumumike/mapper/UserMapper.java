package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectUserByUsername(String username);

    Integer insertUser(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer updateUserInfo(User user);
}
