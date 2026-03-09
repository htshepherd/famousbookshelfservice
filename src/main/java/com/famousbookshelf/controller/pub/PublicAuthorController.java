package com.famousbookshelf.controller.pub;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.entity.Book;
import com.famousbookshelf.service.AuthorService;
import com.famousbookshelf.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C 端公开接口 — 作者详情
 */
@RestController
@RequestMapping("/api/public/authors")
@RequiredArgsConstructor
public class PublicAuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    /**
     * 作者详情
     */
    @GetMapping("/{id}")
    public Result<Author> getAuthorDetail(@PathVariable("id") Long id) {
        Author author = authorService.getById(id);
        if (author == null) {
            return Result.error(404, "作者不存在");
        }
        return Result.success(author);
    }

    /**
     * 该作者创作的所有图书
     */
    @GetMapping("/{id}/books")
    public Result<List<Book>> getAuthorBooks(@PathVariable("id") Long authorId) {
        List<Book> books = bookService.list(
                new LambdaQueryWrapper<Book>()
                        .eq(Book::getAuthorId, authorId)
                        .orderByDesc(Book::getUpdatedAt)
        );
        return Result.success(books);
    }
}
