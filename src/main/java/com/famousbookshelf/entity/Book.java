package com.famousbookshelf.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书实体
 */
@Data
@TableName("book")
public class Book {

    @TableId(value = "book_id", type = IdType.AUTO)
    private Long bookId;

    private String chineseName;

    private String englishName;

    private Long authorId;

    private String overview;

    private String coverUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Boolean isDeleted;
}
