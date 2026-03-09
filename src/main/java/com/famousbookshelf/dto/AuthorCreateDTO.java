package com.famousbookshelf.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class AuthorCreateDTO {

    @NotBlank(message = "作者中文名不能为空")
    @Size(max = 100, message = "作者中文名长度不能超过100个字符")
    private String chineseName;

    @Size(max = 100, message = "作者英文名长度不能超过100个字符")
    private String englishName;
}
