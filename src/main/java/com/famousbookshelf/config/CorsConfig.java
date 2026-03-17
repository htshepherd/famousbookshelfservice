package com.famousbookshelf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 全局 CORS 跨域配置
 * 允许本地 Vue 开发端口 (5173/5174/3000) 跨域访问
 */
@Configuration
public class CorsConfig {

    @org.springframework.beans.factory.annotation.Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许的前端源地址
        config.setAllowedOrigins(allowedOrigins);

        // 允许所有请求方法
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 允许所有请求头
        config.setAllowedHeaders(List.of("*"));

        // 允许携带 Cookie / Authorization
        config.setAllowCredentials(true);

        // 预检请求缓存时间（秒）
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
