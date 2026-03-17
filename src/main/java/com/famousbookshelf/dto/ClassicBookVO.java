package com.famousbookshelf.dto;

import lombok.Data;

@Data
public class ClassicBookVO {
    private Long bookId;
    private String chineseName;
    private String coverUrl;
    private String authorChineseName;
    private Long recommendationCount;
    private String overview;
}
