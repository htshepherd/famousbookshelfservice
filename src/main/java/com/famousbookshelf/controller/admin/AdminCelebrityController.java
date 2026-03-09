package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.service.CelebrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import com.famousbookshelf.dto.CelebrityCreateDTO;
import com.famousbookshelf.dto.CelebrityUpdateDTO;

/**
 * 管理端 — 名人 CRUD
 */
@RestController
@RequestMapping("/api/admin/celebrities")
@RequiredArgsConstructor
public class AdminCelebrityController {

    private final CelebrityService celebrityService;

    /** 分页列表（带关键字搜索，按更新时间倒序） */
    @GetMapping
    @PreAuthorize("hasAuthority('content:celebrity:list')")
    public Result<Page<Celebrity>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {

        LambdaQueryWrapper<Celebrity> wrapper = new LambdaQueryWrapper<>();
        if (org.springframework.util.StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Celebrity::getChineseName, keyword)
                    .or()
                    .like(Celebrity::getEnglishName, keyword)
                    .or()
                    .like(Celebrity::getGroupName, keyword));
        }
        wrapper.orderByDesc(Celebrity::getUpdatedAt);

        Page<Celebrity> result = celebrityService.page(new Page<>(page, size), wrapper);
        return Result.success(result);
    }

    /** 新增 */
    @PostMapping
    @PreAuthorize("hasAuthority('content:celebrity:add')")
    public Result<Celebrity> create(@Validated @RequestBody CelebrityCreateDTO dto) {
        Celebrity celebrity = new Celebrity();
        BeanUtils.copyProperties(dto, celebrity);
        celebrityService.save(celebrity);
        return Result.success(celebrity);
    }

    /** 编辑 */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('content:celebrity:update')")
    public Result<Celebrity> update(@PathVariable("id") Long id, @Validated @RequestBody CelebrityUpdateDTO dto) {
        Celebrity celebrity = new Celebrity();
        BeanUtils.copyProperties(dto, celebrity);
        celebrity.setCelebrityId(id);
        celebrityService.updateById(celebrity);
        return Result.success(celebrity);
    }

    /** 逻辑删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('content:celebrity:delete')")
    public Result<Void> delete(@PathVariable("id") Long id) {
        celebrityService.removeById(id);
        return Result.success();
    }
}
