package com.famousbookshelf.controller.pub;

import com.famousbookshelf.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/public/ping")
    public Result<String> ping() {
        return Result.success("pong");
    }
}
