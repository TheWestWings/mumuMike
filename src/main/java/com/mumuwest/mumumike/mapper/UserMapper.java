package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User selectUserByUsername(String username);

    /**
     * 添加用户
     * @param user
     * @return
     */
    Integer insertUser(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer updateUserInfo(User user);

    /**
     * 选择全部用户
     * @return
     */
    List<User> selectAllUsers(User user);

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    User selectUserById(Integer id);
}
