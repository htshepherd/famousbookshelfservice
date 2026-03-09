package com.famousbookshelf.dto;

import lombok.Data;
import java.util.List;

/**
 * 图书搜索结果 VO
 */
@Data
public class BookSearchResultVO {
    private Long bookId;
    private String chineseName;
    private String englishName;
    private String overview;
    private String coverUrl;

    // 作者中文名
    private String authorChineseName;

    // 推荐人列表
    private List<RecommenderInfoVO> recommenders;

    @Data
    public static class RecommenderInfoVO {
        private Long celebrityId;
        private String chineseName;
        private String groupName;
    }
}
