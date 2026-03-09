package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.dto.ImportResultDTO;
import com.famousbookshelf.entity.Recommendation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 推荐记录 Service 接口
 */
public interface RecommendationService extends IService<Recommendation> {

    /**
     * Excel 批量导入推荐记录
     * 单行隔离容错：任何一行失败不影响其他行
     *
     * @param file 上传的 Excel 文件
     * @return 导入结果（成功数、失败数、错误明细）
     */
    ImportResultDTO importFromExcel(MultipartFile file);

    /**
     * 后台管理分页查询，包含书名、作者名、名人名
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 分页结果
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminRecommendationVO> searchAdminPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminRecommendationVO> page,
            String keyword);
}
