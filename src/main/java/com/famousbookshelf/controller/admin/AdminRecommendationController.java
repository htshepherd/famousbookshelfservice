package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.ImportResultDTO;
import com.famousbookshelf.dto.AdminRecommendationVO;
import com.famousbookshelf.entity.Recommendation;
import com.famousbookshelf.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result<Page<AdminRecommendationVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<AdminRecommendationVO> result = recommendationService.searchAdminPage(new Page<>(page, size), keyword);
        return Result.success(result);
    }

    /** 新增 */
    @PostMapping
    public Result<Recommendation> create(@RequestBody Recommendation recommendation) {
        recommendationService.save(recommendation);
        return Result.success(recommendation);
    }

    /** 编辑 */
    @PutMapping("/{id}")
    public Result<Recommendation> update(@PathVariable("id") Long id,
            @RequestBody Recommendation recommendation) {
        recommendation.setRecordId(id);
        recommendationService.updateById(recommendation);
        return Result.success(recommendation);
    }

    /** 逻辑删除 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        recommendationService.removeById(id);
        return Result.success();
    }

    /**
     * Excel 批量导入推荐记录
     * POST /api/admin/recommendations/import
     */
    @PostMapping("/import")
    public Result<ImportResultDTO> importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        ImportResultDTO importResult = recommendationService.importFromExcel(file);
        return Result.success(importResult);
    }
}
