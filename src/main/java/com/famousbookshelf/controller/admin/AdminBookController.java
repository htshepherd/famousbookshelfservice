package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.Book;
import com.famousbookshelf.dto.AdminBookVO;
import com.famousbookshelf.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import com.famousbookshelf.dto.BookCreateDTO;
import com.famousbookshelf.dto.BookUpdateDTO;

/**
 * 管理端 — 图书 CRUD
 */
@RestController
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    /** 分页列表（带关键字搜索以及关联作者名，按更新时间倒序） */
    @GetMapping
    @PreAuthorize("hasAuthority('content:book:list')")
    public Result<Page<AdminBookVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<AdminBookVO> result = bookService.searchAdminPage(new Page<>(page, size), keyword);
        return Result.success(result);
    }

    /** 新增 */
    @PostMapping
    @PreAuthorize("hasAuthority('content:book:add')")
    public Result<Book> create(@Validated @RequestBody BookCreateDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        bookService.save(book);
        return Result.success(book);
    }

    /** 编辑 */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('content:book:update')")
    public Result<Book> update(@PathVariable("id") Long id, @Validated @RequestBody BookUpdateDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        book.setBookId(id);
        bookService.updateById(book);
        return Result.success(book);
    }

    /** 逻辑删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('content:book:delete')")
    public Result<Void> delete(@PathVariable("id") Long id) {
        bookService.removeById(id);
        return Result.success();
    }
}
