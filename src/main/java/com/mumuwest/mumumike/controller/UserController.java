package com.mumuwest.mumumike.controller;


import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.mapper.UserMapper;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.UserService;
import com.mumuwest.mumumike.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/updateRole")
    @Role(role = {1, 2})
    public AjaxResult updateRole(){
        return AjaxResult.success();
    }

}
