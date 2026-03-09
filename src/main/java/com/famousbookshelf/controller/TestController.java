package com.famousbookshelf.controller;

import com.famousbookshelf.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 连通性测试接口
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/api/test-db")
    public Result<String> testDb() {
        try {
            Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Result.success("Database connection OK: " + one);
        } catch (Exception e) {
            return Result.error("Database connection failed: " + e.getMessage());
        }
    }
}
