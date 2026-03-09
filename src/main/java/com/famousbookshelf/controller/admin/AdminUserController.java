package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.UserVO;
import com.famousbookshelf.entity.User;
import com.famousbookshelf.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<UserVO>> list(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(userService.getAdminPage(new Page<>(current, size)));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Boolean> add(@RequestBody User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return Result.success(userService.save(user));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:user:update')")
    public Result<Boolean> update(@RequestBody User user) {
        // Clear password if not update
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return Result.success(userService.updateById(user));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/assignRoles/{userId}")
    @PreAuthorize("hasAuthority('system:user:assign')")
    public Result<Void> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        userService.assignRoles(userId, roleIds);
        return Result.success();
    }
}
