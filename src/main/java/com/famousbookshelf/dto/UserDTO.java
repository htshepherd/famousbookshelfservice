package com.famousbookshelf.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
public class UserDTO {
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    private Integer status;
}
