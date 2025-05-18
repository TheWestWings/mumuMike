package com.mumuwest.mumumike.controller;


import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.mapper.UserMapper;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.UserService;
import com.mumuwest.mumumike.utils.FileStorageUtil;
import com.mumuwest.mumumike.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Update;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            // 生成 JWT token
            user = userService.getUserByUsername(user.getUsername());
            String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
            return new AjaxResult(200, "登录成功", token);
        } catch (AuthenticationException e) {
            return AjaxResult.error("用户名或密码错误");
        }
    }

    /**
     * 注册
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody User registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.selectUserByUsername(registerRequest.getUsername()) != null) {
            return new AjaxResult(403, "用户名已存在");
        }
        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 加密密码
        userService.register(user.getUsername(), user.getPassword(), registerRequest.getEmail(), registerRequest.getPhone());

        return AjaxResult.success("注册成功");
    }

    /**
     * 更新角色，只有super可以修改
     * @param user
     * @return
     */
    @PutMapping("/updateRole")
    @Role(role = {0})
    public AjaxResult updateRole(@RequestBody User user) {
        User userUpdate = new User();
        userUpdate.setRole(user.getRole());
        userUpdate.setId(user.getId());
        return AjaxResult.success(userService.updateUserInfo(userUpdate));
    }

    /**
     * 更新user状态， 只有管理员可以更新
     * @param user
     * @return
     */
    @PutMapping("/updateStatus")
    @Role(role = {0, 1})
    public AjaxResult updateStatus(@RequestBody User user) {
        User userUpdate = new User();
        userUpdate.setStatus(user.getStatus());
        userUpdate.setId(user.getId());
        return AjaxResult.success(userService.updateUserInfo(userUpdate));
    }

    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("/getList")
    @Role(role = {0, 1})
    public TableDataInfo list(User user) {
        TableDataInfo tableDataInfo = new TableDataInfo(userService.getList(user), userMapper.selectAllUsers().size());
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        return tableDataInfo;
    }

    /**
     * 获取本人信息
     * @param request
     * @return
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(HttpServletRequest request) {
        // 通过jwt另外解析用户名
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        String username = JwtUtil.getUsernameFromToken(token);
        return AjaxResult.success(userService.getUserByUsername(username));
    }

    /**
     * 根据id获取用户信息，只有管理员可以
     * @param id
     * @return
     */
    @GetMapping("/getUserById/{id}")
    @Role(role = {0, 1})
    public AjaxResult getUserById(@PathVariable("id") Integer id) {
        return AjaxResult.success(userService.getUserById(id));
    }

    /**
     * 根据id修改用户信息，只有管理员可以
     * @param user
     * @return
     */
    @PutMapping("/updateUserByid")
    @Role(role = {0, 1})
    public AjaxResult updateUserByid(@RequestBody User user) {
        return AjaxResult.success(userService.updateUserInfo(user));
    }

    /**
     * 修改自身信息
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/updateUser")
    public AjaxResult updateUser(@RequestBody User user, HttpServletRequest request) {
        // 通过jwt另外解析用户名
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        String username = JwtUtil.getUsernameFromToken(token);
        User userByUsername = userService.getUserByUsername(username);
        User userUpdate = new User();
        userUpdate.setId(userByUsername.getId());
        userUpdate.setUsername(user.getUsername());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPhone(user.getPhone());
        return AjaxResult.success(userService.updateUserInfo(user));
    }

    /**
     * 修改头像
     * @param avatar
     * @param request
     * @return
     */
    @PostMapping("/updateAvatar")
    public AjaxResult updateAvatar(@RequestParam("avatar") MultipartFile avatar, HttpServletRequest request) throws IOException {
        // 通过jwt另外解析用户名
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        String username = JwtUtil.getUsernameFromToken(token);
        User userByUsername = userService.getUserByUsername(username);
        User userUpdate = new User();
        userUpdate.setId(userByUsername.getId());
        if(avatar != null) {
            String avatarUrl = FileStorageUtil.storeFile(avatar);
            userUpdate.setAvatar(avatarUrl);
        }
        return AjaxResult.success(userService.updateUserInfo(userUpdate));
    }

}
