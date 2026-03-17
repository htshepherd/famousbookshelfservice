package com.famousbookshelf.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @org.springframework.beans.factory.annotation.Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * 处理 JWT 相关异常
     */
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public Result<Void> handleJwtException(io.jsonwebtoken.JwtException e) {
        log.warn("JWT 校验异常: {}", e.getMessage());
        if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
            return Result.error(401, "Token 已过期");
        }
        return Result.error(401, "无效的 Token");
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        log.warn("访问拒绝: {}", e.getMessage());
        return Result.error(403, "权限不足");
    }

    /**
     * 处理认证异常 (如密码错误)
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public Result<Void> handleAuthenticationException(org.springframework.security.core.AuthenticationException e) {
        log.warn("认证失败: {}", e.getMessage());
        return Result.error(401, "用户名或密码错误");
    }

    /**
     * 处理所有未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        String message = "prod".equals(activeProfile) ? "服务器内部错误" : "系统错误: " + e.getMessage();
        return Result.error(500, message);
    }

}
