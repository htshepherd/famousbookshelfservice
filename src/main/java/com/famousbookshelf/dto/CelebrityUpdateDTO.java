package com.famousbookshelf.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CelebrityUpdateDTO {

    @NotBlank(message = "名人中文名不能为空")
    @Size(max = 100, message = "名人中文名长度不能超过100个字符")
    private String chineseName;

    @Size(max = 100, message = "名人英文名长度不能超过100个字符")
    private String englishName;

    @Size(max = 100, message = "组别名称长度不能超过100个字符")
    private String groupName;

    @Size(max = 1000, message = "头像URL不能超过1000个字符")
    private String avatarUrl;
}
