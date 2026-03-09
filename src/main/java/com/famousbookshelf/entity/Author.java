package com.famousbookshelf.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作者实体
 */
@Data
@TableName("author")
public class Author {

    @TableId(value = "author_id", type = IdType.AUTO)
    private Long authorId;

    private String chineseName;

    private String englishName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Boolean isDeleted;
}
