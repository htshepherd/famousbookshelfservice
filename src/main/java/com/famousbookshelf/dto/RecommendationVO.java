package com.famousbookshelf.dto;

import lombok.Data;

/**
 * 推荐记录 VO（含名人/图书关联信息，供详情页使用）
 */
@Data
public class RecommendationVO {

    private Long recordId;
    private String sourceDescription;
    private String evidenceSummary;
    private String reliability;
    private String evidenceUrl;
    private String briefOverview;

    // 关联的图书信息
    private Long bookId;
    private String bookChineseName;
    private String bookEnglishName;
    private String bookCoverUrl;
    private String bookOverview;
    private String authorChineseName;

    // 关联的名人信息
    private Long celebrityId;
    private String celebrityChineseName;
    private String celebrityEnglishName;
    private String celebrityGroupName;
    private String celebrityAvatarUrl;
}
