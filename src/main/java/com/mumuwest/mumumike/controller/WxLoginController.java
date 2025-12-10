package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.UserService;
import com.mumuwest.mumumike.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信小程序登录控制器
 */
@RestController
@RequestMapping("/wx")
public class WxLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 微信小程序配置，需要在application.properties中配置
    @Value("${wx.appid:}")
    private String appid;

    @Value("${wx.secret:}")
    private String secret;

    /**
     * 微信小程序登录
     * @param params 包含code的参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public AjaxResult wxLogin(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        
        if (code == null || code.isEmpty()) {
            return AjaxResult.error("code不能为空");
        }

        try {
            // 调用微信接口获取openid
            String openid = getOpenidFromWx(code);
            
            if (openid == null || openid.isEmpty()) {
                return AjaxResult.error("微信登录失败");
            }

            // 根据openid查找或创建用户
            User user = userService.getUserByOpenid(openid);
            
            if (user == null) {
                // 自动注册新用户
                user = new User();
                user.setUsername("wx_" + openid.substring(0, 8));
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setRole(2); // 普通用户
                user.setStatus("1");
                // 注意：需要在User实体和数据库中添加openid字段
                userService.register(user.getUsername(), user.getPassword(), null, null);
                user = userService.getUserByUsername(user.getUsername());
            }

            // 生成token
            String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", user);

            return AjaxResult.success(result);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 调用微信接口获取openid
     * @param code 微信登录code
     * @return openid
     */
    private String getOpenidFromWx(String code) {
        // 如果未配置appid和secret，使用模拟方式（开发测试用）
        if (appid == null || appid.isEmpty() || secret == null || secret.isEmpty()) {
            // 开发模式：直接返回基于code的模拟openid
            return "mock_openid_" + code.hashCode();
        }

        try {
            String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, secret, code
            );

            RestTemplate restTemplate = new RestTemplate();
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("openid")) {
                return (String) response.get("openid");
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
