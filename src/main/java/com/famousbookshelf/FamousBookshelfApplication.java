package com.famousbookshelf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 名人书架 — Spring Boot 启动类
 */
@SpringBootApplication
@MapperScan("com.famousbookshelf.mapper")
public class FamousBookshelfApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamousBookshelfApplication.class, args);
    }
}
