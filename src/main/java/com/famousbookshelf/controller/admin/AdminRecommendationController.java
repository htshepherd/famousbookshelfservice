package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.ImportResultDTO;
import com.famousbookshelf.dto.AdminRecommendationVO;
import com.famousbookshelf.entity.Recommendation;
import com.famousbookshelf.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.BeanUtils;
import com.famousbookshelf.dto.RecommendationCreateDTO;
import com.famousbookshelf.dto.RecommendationUpdateDTO;

/**
 * 管理端 — 推荐记录 CRUD + Excel 导入
 */
@RestController
@RequestMapping("/api/admin/recommendations")
@RequiredArgsConstructor
public class AdminRecommendationController {

    private final RecommendationService recommendationService;

    /** 分页列表（带关键字搜索及关联表名，按更新时间倒序） */
    @GetMapping
    @PreAuthorize("hasAuthority('content:recommendation:list')")
    public Result<Page<AdminRecommendationVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<AdminRecommendationVO> result = recommendationService.searchAdminPage(new Page<>(page, size), keyword);
        return Result.success(result);
    }

    /** 新增 */
    @PostMapping
    @PreAuthorize("hasAuthority('content:recommendation:add')")
    public Result<Recommendation> create(@Validated @RequestBody RecommendationCreateDTO dto) {
        Recommendation recommendation = new Recommendation();
        BeanUtils.copyProperties(dto, recommendation);
        recommendationService.save(recommendation);
        return Result.success(recommendation);
    }

    /** 编辑 */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('content:recommendation:update')")
    public Result<Recommendation> update(@PathVariable("id") Long id,
            @Validated @RequestBody RecommendationUpdateDTO dto) {
        Recommendation recommendation = new Recommendation();
        BeanUtils.copyProperties(dto, recommendation);
        recommendation.setRecordId(id);
        recommendationService.updateById(recommendation);
        return Result.success(recommendation);
    }

    /** 逻辑删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('content:recommendation:delete')")
    public Result<Void> delete(@PathVariable("id") Long id) {
        recommendationService.removeById(id);
        return Result.success();
    }

    /**
     * Excel 批量导入推荐记录
     * POST /api/admin/recommendations/import
     */
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('tool:import:all')")
    public Result<ImportResultDTO> importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        ImportResultDTO importResult = recommendationService.importFromExcel(file);
        return Result.success(importResult);
    }
}
