package com.famousbookshelf.dto;

import com.famousbookshelf.entity.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminBookVO extends Book {
    private String authorName; // 作者中文名或英文名
}
