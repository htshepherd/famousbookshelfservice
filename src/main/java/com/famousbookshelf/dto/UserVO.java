package com.famousbookshelf.dto;

import com.famousbookshelf.entity.SysRole;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private Integer status;
    private LocalDateTime createdAt;
    private List<SysRole> roles;
}
