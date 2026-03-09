package com.famousbookshelf.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书详情 VO（含作者信息）
 */
@Data
public class BookDetailVO {

    private Long bookId;
    private String chineseName;
    private String englishName;
    private String coverUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long authorId;
    private String authorChineseName;
    private String authorEnglishName;

    // 图书概览
    private String overview;

    // 推荐记录列表
    private java.util.List<RecommendationVO> recommendations;
}
