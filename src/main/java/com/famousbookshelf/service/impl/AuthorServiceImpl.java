package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.mapper.AuthorMapper;
import com.famousbookshelf.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 作者 Service 实现
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {

    @Override
    public Author findOrCreate(String chineseName, String englishName) {
        String trimCn = StringUtils.hasText(chineseName) ? chineseName.trim() : null;
        String trimEn = StringUtils.hasText(englishName) ? englishName.trim() : null;

        // 构建查询条件：优先用中文名匹配，若中文名为空则用英文名
        LambdaQueryWrapper<Author> wrapper = new LambdaQueryWrapper<>();
        if (trimCn != null) {
            wrapper.eq(Author::getChineseName, trimCn);
        }
        if (trimEn != null) {
            wrapper.eq(Author::getEnglishName, trimEn);
        }

        Author existing = this.getOne(wrapper, false);
        if (existing != null) {
            return existing;
        }

        // 不存在，新建作者
        Author author = new Author();
        author.setChineseName(trimCn);
        author.setEnglishName(trimEn);
        this.save(author);
        return author;
    }
}
