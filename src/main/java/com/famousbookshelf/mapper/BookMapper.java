package com.famousbookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.famousbookshelf.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.dto.AdminBookVO;
import com.famousbookshelf.dto.ClassicBookVO;

import java.util.List;

/**
 * 图书 Mapper 接口
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    @Select("<script>" +
            "SELECT b.*, a.chinese_name as author_name " +
            "FROM book b " +
            "LEFT JOIN author a ON b.author_id = a.author_id " +
            "WHERE b.is_deleted = false " +
            "<if test='keyword != null and keyword != \"\"'>" +
            " AND (b.chinese_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR b.english_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR a.chinese_name LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            " ORDER BY b.updated_at DESC" +
            "</script>")
    Page<AdminBookVO> searchAdminPage(Page<AdminBookVO> page, @Param("keyword") String keyword);

    @Select("SELECT b.book_id, b.chinese_name, b.cover_url, b.overview, a.chinese_name as author_chinese_name, COUNT(r.record_id) as recommendation_count " +
            "FROM book b " +
            "INNER JOIN author a ON b.author_id = a.author_id " +
            "LEFT JOIN recommendation r ON b.book_id = r.book_id AND r.is_deleted = false " +
            "WHERE b.is_deleted = false " +
            "GROUP BY b.book_id, b.chinese_name, b.cover_url, b.overview, a.chinese_name " +
            "HAVING COUNT(r.record_id) >= 3 " +
            "ORDER BY recommendation_count DESC")
    List<ClassicBookVO> getClassicBooks();
}
