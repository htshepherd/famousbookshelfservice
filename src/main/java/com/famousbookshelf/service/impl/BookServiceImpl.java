package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.dto.BookSearchResultVO;
import com.famousbookshelf.dto.ClassicBookVO;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.entity.Book;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.entity.Recommendation;
import com.famousbookshelf.mapper.AuthorMapper;
import com.famousbookshelf.mapper.BookMapper;
import com.famousbookshelf.mapper.CelebrityMapper;
import com.famousbookshelf.mapper.RecommendationMapper;
import com.famousbookshelf.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图书 Service 实现
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final AuthorMapper authorMapper;
    private final RecommendationMapper recommendationMapper;
    private final CelebrityMapper celebrityMapper;

    @Override
    public Book findOrCreate(String chineseName, String englishName, Long authorId, String coverUrl, String overview) {
        String trimCn = StringUtils.hasText(chineseName) ? chineseName.trim() : null;
        String trimEn = StringUtils.hasText(englishName) ? englishName.trim() : null;
        String trimCover = StringUtils.hasText(coverUrl) ? coverUrl.trim() : null;
        String trimOverview = StringUtils.hasText(overview) ? overview.trim() : null;

        // 根据"书名 + 作者ID"查找
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getAuthorId, authorId);
        if (trimCn != null) {
            wrapper.eq(Book::getChineseName, trimCn);
        }
        if (trimEn != null) {
            wrapper.eq(Book::getEnglishName, trimEn);
        }

        Book existing = this.getOne(wrapper, false);
        if (existing != null) {
            boolean updated = false;
            // 存在 → 选择性更新 cover_url（仅当新值非空且旧值为空时更新）
            if (trimCover != null && !StringUtils.hasText(existing.getCoverUrl())) {
                existing.setCoverUrl(trimCover);
                updated = true;
            }
            // 选择性更新 overview（仅当新值非空且旧值为空时更新）
            if (trimOverview != null && !StringUtils.hasText(existing.getOverview())) {
                existing.setOverview(trimOverview);
                updated = true;
            }
            if (updated) {
                this.updateById(existing);
            }
            return existing;
        }

        // 不存在，新建图书
        Book book = new Book();
        book.setChineseName(trimCn);
        book.setEnglishName(trimEn);
        book.setAuthorId(authorId);
        book.setCoverUrl(trimCover);
        book.setOverview(trimOverview);
        this.save(book);
        return book;
    }

    @Override
    public List<BookSearchResultVO> searchBooksEnriched(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }

        // 1. 基础搜索
        List<Book> books = this.list(new LambdaQueryWrapper<Book>()
                .like(Book::getChineseName, keyword.trim())
                .or()
                .like(Book::getEnglishName, keyword.trim()));

        if (CollectionUtils.isEmpty(books)) {
            return Collections.emptyList();
        }

        List<Long> bookIds = books.stream().map(Book::getBookId).toList();
        List<Long> authorIds = books.stream().map(Book::getAuthorId).distinct().toList();

        // 2. 批量查作者
        Map<Long, String> authorNameMap = authorMapper.selectBatchIds(authorIds).stream()
                .collect(Collectors.toMap(Author::getAuthorId, Author::getChineseName, (v1, v2) -> v1));

        // 3. 批量查推荐记录
        List<Recommendation> recs = recommendationMapper.selectList(new LambdaQueryWrapper<Recommendation>()
                .in(Recommendation::getBookId, bookIds));

        // 4. 批量查推荐人（名人）信息
        Map<Long, Celebrity> celebrityMap = Collections.emptyMap();
        if (!recs.isEmpty()) {
            List<Long> celebIds = recs.stream().map(Recommendation::getCelebrityId).distinct().toList();
            List<Celebrity> celebs = celebrityMapper.selectBatchIds(celebIds);
            if (celebs != null) {
                celebrityMap = celebs.stream()
                        .collect(Collectors.toMap(Celebrity::getCelebrityId, c -> c));
            }
        }

        // 5. 组装结果
        Map<Long, List<Recommendation>> bookRecMap = recs.stream()
                .collect(Collectors.groupingBy(Recommendation::getBookId));

        List<BookSearchResultVO> result = new ArrayList<>();
        for (Book book : books) {
            BookSearchResultVO vo = new BookSearchResultVO();
            vo.setBookId(book.getBookId());
            vo.setChineseName(book.getChineseName());
            vo.setEnglishName(book.getEnglishName());
            vo.setOverview(book.getOverview());
            vo.setCoverUrl(book.getCoverUrl());
            vo.setAuthorChineseName(authorNameMap.get(book.getAuthorId()));

            List<Recommendation> bookRecs = bookRecMap.getOrDefault(book.getBookId(), Collections.emptyList());
            List<BookSearchResultVO.RecommenderInfoVO> recommenders = new ArrayList<>();
            for (Recommendation rec : bookRecs) {
                Celebrity celeb = celebrityMap.get(rec.getCelebrityId());
                if (celeb != null) {
                    BookSearchResultVO.RecommenderInfoVO ri = new BookSearchResultVO.RecommenderInfoVO();
                    ri.setCelebrityId(celeb.getCelebrityId());
                    ri.setChineseName(celeb.getChineseName());
                    ri.setGroupName(celeb.getGroupName());
                    recommenders.add(ri);
                }
            }
            vo.setRecommenders(recommenders);
            result.add(vo);
        }

        return result;
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminBookVO> searchAdminPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminBookVO> page,
            String keyword) {
        return baseMapper.searchAdminPage(page, keyword);
    }

    @Override
    public List<ClassicBookVO> getClassicBooks() {
        return baseMapper.getClassicBooks();
    }
}
