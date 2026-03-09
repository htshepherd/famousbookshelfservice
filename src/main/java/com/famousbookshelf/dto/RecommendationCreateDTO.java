package com.famousbookshelf.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class RecommendationCreateDTO {

    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    @NotNull(message = "名人ID不能为空")
    private Long celebrityId;

    @Size(max = 2000, message = "来源描述长度不能超过2000个字符")
    private String sourceDescription;

    @Size(max = 2000, message = "推荐理由长度不能超过2000个字符")
    private String evidenceSummary;

    @Size(max = 50, message = "可靠度分类长度不能超过50个字符")
    private String reliability;

    @Size(max = 1000, message = "引用链接长度不能超过1000个字符")
    private String evidenceUrl;

    @Size(max = 1000, message = "简短概述长度不能超过1000个字符")
    private String briefOverview;
}
