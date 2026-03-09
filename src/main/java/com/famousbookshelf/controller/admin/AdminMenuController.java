package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.SysMenu;
import com.famousbookshelf.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@RequiredArgsConstructor
public class AdminMenuController {

    private final SysMenuService menuService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<SysMenu>> list() {
        return Result.success(menuService.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder)));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Boolean> add(@RequestBody SysMenu menu) {
        return Result.success(menuService.save(menu));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:menu:update')")
    public Result<Boolean> update(@RequestBody SysMenu menu) {
        return Result.success(menuService.updateById(menu));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(menuService.removeById(id));
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<SysMenu>> tree() {
        // Simple tree building can be done here or in service.
        // For simplicity, we just return all and let frontend handle it.
        return Result.success(menuService.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder)));
    }
}
