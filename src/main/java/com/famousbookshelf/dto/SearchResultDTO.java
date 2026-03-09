package com.famousbookshelf.dto;

import lombok.Data;

import java.util.List;

/**
 * 全局搜索结果 DTO
 */
@Data
public class SearchResultDTO {

    private List<BookSearchResultVO> books;
    private List<?> celebrities;
    private List<?> authors;
}
