package com.famousbookshelf.controller.pub;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.SearchResultDTO;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.service.AuthorService;
import com.famousbookshelf.service.BookService;
import com.famousbookshelf.service.CelebrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * C 端公开接口 — 全局搜索
 * 使用 PostgreSQL ILIKE 实现大小写不敏感的模糊搜索
 */
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicSearchController {

        private final BookService bookService;
        private final CelebrityService celebrityService;
        private final AuthorService authorService;

        @GetMapping("/search")
        public Result<SearchResultDTO> search(@RequestParam("q") String keyword) {
                SearchResultDTO result = new SearchResultDTO();

                if (!StringUtils.hasText(keyword)) {
                        result.setBooks(Collections.emptyList());
                        result.setCelebrities(Collections.emptyList());
                        result.setAuthors(Collections.emptyList());
                        return Result.success(result);
                }

                if (keyword.length() > 100) {
                        return Result.error(400, "搜索关键词过长");
                }


                // 搜索图书（中文名或英文名 ILIKE）
                result.setBooks(bookService.searchBooksEnriched(keyword));
                // 搜索名人
                List<Celebrity> celebrities = celebrityService.list(
                                new LambdaQueryWrapper<Celebrity>()
                                                .like(Celebrity::getChineseName, keyword.trim())
                                                .or()
                                                .like(Celebrity::getEnglishName, keyword.trim()));
                // 搜索作者
                List<Author> authors = authorService.list(
                                new LambdaQueryWrapper<Author>()
                                                .like(Author::getChineseName, keyword.trim())
                                                .or()
                                                .like(Author::getEnglishName, keyword.trim()));
                result.setCelebrities(celebrities);
                result.setAuthors(authors);
                return Result.success(result);
        }
}
