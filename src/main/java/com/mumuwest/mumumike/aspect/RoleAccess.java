package com.mumuwest.mumumike.aspect;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


@Component
@Aspect
public class RoleAccess {

    // 定义切点，匹配带有 @Role 注解的方法
    @Pointcut("@annotation(com.mumuwest.mumumike.annotation.Role)")
    public void checkRole() {
    }

    // 环绕通知，用于处理带有 @Role 注解的方法
    @Around("checkRole()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取目标方法
        java.lang.reflect.Method method = signature.getMethod();
        // 获取方法上的 @Role 注解
        Role roleAnnotation = method.getAnnotation(Role.class);

        if (roleAnnotation != null) {
            // 取出注解中的 role 值
            int[] roleValue = roleAnnotation.role();
            System.out.println("Role value from annotation: " + Arrays.toString(roleValue));
            // 取出HttpServletRequest对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 从请求头中获取 JWT token
            String token = request.getHeader("Authorization");
            token = token.substring(7);
            System.out.println("Token from request header: " + token);
            // 从 JWT token 中获取角色信息
            int role = JwtUtil.getRoleFromToken(token);
            System.out.println("Role from JWT token: " + role);

        }


        // 继续执行目标方法
        return joinPoint.proceed();
    }
}
