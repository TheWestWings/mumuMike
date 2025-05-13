package com.mumuwest.mumumike.aspect;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


@Component
@Aspect
@Slf4j
public class RoleAccess {
    // Manual logger initialization as a backup
    private static final Logger logger = LoggerFactory.getLogger(RoleAccess.class);

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
            // 取出HttpServletRequest对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 从请求头中获取 JWT token
            String token = request.getHeader("Authorization");
            token = token.substring(7);

            // 从 JWT token 中获取角色信息
            int role = JwtUtil.getRoleFromToken(token);
            logger.info("可以用的角色: " + Arrays.toString(roleValue) + " 当前角色: " + role);
            // 检查当前角色是否在允许的角色列表中
            boolean hasRequiredRole = Arrays.stream(roleValue).anyMatch(r -> r == role);
            if (!hasRequiredRole) {
                logger.warn("Access denied for role: " + role);
                return AjaxResult.error(403, "没有权限访问该资源");
            }
        }

        // 继续执行目标方法
        return joinPoint.proceed();
    }
}
