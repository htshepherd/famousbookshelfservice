package com.famousbookshelf.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 名人实体
 */
@Data
@TableName("celebrity")
public class Celebrity {

    @TableId(value = "celebrity_id", type = IdType.AUTO)
    private Long celebrityId;

    private String chineseName;

    private String englishName;

    private String groupName;

    private String avatarUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Boolean isDeleted;
}
