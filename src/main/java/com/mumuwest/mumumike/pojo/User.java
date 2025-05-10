package com.mumuwest.mumumike.pojo;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer role;   // 0: 超级管理元, 1: 管理员，2：普通用户
    private String email;
    private String phone;
}

