package com.famousbookshelf.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书推荐记录实体
 */
@Data
@TableName("recommendation")
public class Recommendation {

    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    private Long bookId;

    private Long celebrityId;

    private String sourceDescription;

    private String evidenceSummary;

    private String reliability;

    private String evidenceUrl;

    private String briefOverview;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Boolean isDeleted;
}
