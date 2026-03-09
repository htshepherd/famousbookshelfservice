package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.SysRole;
import com.famousbookshelf.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
public class AdminRoleController {

    private final SysRoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.list(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getRoleId)));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Boolean> add(@RequestBody SysRole role) {
        return Result.success(roleService.save(role));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:role:update')")
    public Result<Boolean> update(@RequestBody SysRole role) {
        return Result.success(roleService.updateById(role));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(roleService.removeById(id));
    }

    @PostMapping("/assignMenus/{roleId}")
    @PreAuthorize("hasAuthority('system:role:assign')")
    public Result<Void> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.assignMenus(roleId, menuIds);
        return Result.success();
    }
}
