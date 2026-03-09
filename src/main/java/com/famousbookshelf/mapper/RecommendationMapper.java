package com.famousbookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.famousbookshelf.entity.Recommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.dto.AdminRecommendationVO;

/**
 * 推荐记录 Mapper 接口
 */
@Mapper
public interface RecommendationMapper extends BaseMapper<Recommendation> {

    @Select("<script>" +
            "SELECT r.*, b.chinese_name AS book_name, " +
            "a.chinese_name AS author_name, " +
            "c.chinese_name AS celebrity_name " +
            "FROM recommendation r " +
            "LEFT JOIN book b ON r.book_id = b.book_id " +
            "LEFT JOIN author a ON b.author_id = a.author_id " +
            "LEFT JOIN celebrity c ON r.celebrity_id = c.celebrity_id " +
            "WHERE r.is_deleted = false " +
            "<if test='keyword != null and keyword != \"\"'>" +
            " AND (b.chinese_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR a.chinese_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR c.chinese_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR r.evidence_summary LIKE CONCAT('%', #{keyword}, '%') " +
            " OR r.source_description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            " ORDER BY r.updated_at DESC" +
            "</script>")
    Page<AdminRecommendationVO> searchAdminPage(Page<AdminRecommendationVO> page, @Param("keyword") String keyword);
}
