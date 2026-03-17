package com.famousbookshelf.controller;

import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.LoginRequestDTO;
import com.famousbookshelf.dto.LoginResponseDTO;
import com.famousbookshelf.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final com.famousbookshelf.security.LoginRateLimiter rateLimiter;


    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Validated @RequestBody LoginRequestDTO request) {
        if (!rateLimiter.tryConsume(request.getUsername())) {
            return Result.error(429, "登录尝试次数过多，请稍后再试");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));


        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(request.getUsername());
            return Result.success(new LoginResponseDTO(token, request.getUsername()));
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
