package com.famousbookshelf.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class BookUpdateDTO {

    @NotBlank(message = "图书中文名不能为空")
    @Size(max = 255, message = "图书中文名长度不能超过255个字符")
    private String chineseName;

    @Size(max = 255, message = "图书英文名长度不能超过255个字符")
    private String englishName;

    private Long authorId;

    @Size(max = 2000, message = "图书简介长度不能超过2000个字符")
    private String overview;

    @Size(max = 1000, message = "封面URL长度不能超过1000个字符")
    private String coverUrl;
}
