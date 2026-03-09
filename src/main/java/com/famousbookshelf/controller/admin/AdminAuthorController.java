package com.famousbookshelf.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端 — 作者 CRUD
 */
@RestController
@RequestMapping("/api/admin/authors")
@RequiredArgsConstructor
public class AdminAuthorController {

    private final AuthorService authorService;

    /** 分页列表（带关键字搜索，按更新时间倒序） */
    @GetMapping
    public Result<Page<Author>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {

        LambdaQueryWrapper<Author> wrapper = new LambdaQueryWrapper<>();
        if (org.springframework.util.StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Author::getChineseName, keyword)
                    .or()
                    .like(Author::getEnglishName, keyword));
        }
        wrapper.orderByDesc(Author::getUpdatedAt);

        Page<Author> result = authorService.page(new Page<>(page, size), wrapper);
        return Result.success(result);
    }

    /** 新增 */
    @PostMapping
    public Result<Author> create(@RequestBody Author author) {
        authorService.save(author);
        return Result.success(author);
    }

    /** 编辑 */
    @PutMapping("/{id}")
    public Result<Author> update(@PathVariable("id") Long id, @RequestBody Author author) {
        author.setAuthorId(id);
        authorService.updateById(author);
        return Result.success(author);
    }

    /** 逻辑删除 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        authorService.removeById(id);
        return Result.success();
    }
}
