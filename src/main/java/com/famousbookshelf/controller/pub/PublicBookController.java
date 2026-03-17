package com.famousbookshelf.controller.pub;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
import com.famousbookshelf.dto.BookDetailVO;
import com.famousbookshelf.dto.ClassicBookVO;
import com.famousbookshelf.dto.RecommendationVO;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.entity.Book;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.entity.Recommendation;
import com.famousbookshelf.service.AuthorService;
import com.famousbookshelf.service.BookService;
import com.famousbookshelf.service.CelebrityService;
import com.famousbookshelf.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * C 端公开接口 — 图书详情
 */
@RestController
@RequestMapping("/api/public/books")
@RequiredArgsConstructor
public class PublicBookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final RecommendationService recommendationService;
    private final CelebrityService celebrityService;

    /**
     * 图书详情（含作者信息）
     */
    @GetMapping("/{id}")
    public Result<BookDetailVO> getBookDetail(@PathVariable("id") Long id) {
        Book book = bookService.getById(id);
        if (book == null) {
            return Result.error(404, "图书不存在");
        }

        BookDetailVO vo = new BookDetailVO();
        vo.setBookId(book.getBookId());
        vo.setChineseName(book.getChineseName());
        vo.setEnglishName(book.getEnglishName());
        vo.setCoverUrl(book.getCoverUrl());
        vo.setOverview(book.getOverview());
        vo.setCreatedAt(book.getCreatedAt());
        vo.setUpdatedAt(book.getUpdatedAt());

        // 查询作者
        Author author = authorService.getById(book.getAuthorId());
        if (author != null) {
            vo.setAuthorId(author.getAuthorId());
            vo.setAuthorChineseName(author.getChineseName());
            vo.setAuthorEnglishName(author.getEnglishName());
        }

        // 查询推荐记录
        vo.setRecommendations(getBookRecommendations(id).getData());

        return Result.success(vo);
    }

    /**
     * 获取推荐过该书的名人列表及推荐详情
     */
    @GetMapping("/{id}/recommendations")
    public Result<List<RecommendationVO>> getBookRecommendations(@PathVariable("id") Long bookId) {
        Book book = bookService.getById(bookId);
        if (book == null) {
            return Result.success(new ArrayList<>());
        }

        // 查询作者信息
        Author author = authorService.getById(book.getAuthorId());
        String authorName = (author != null) ? author.getChineseName() : null;

        List<Recommendation> recs = recommendationService.list(
                new LambdaQueryWrapper<Recommendation>()
                        .eq(Recommendation::getBookId, bookId));

        if (recs.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        // 批量查询名人信息，避免 N+1
        List<Long> celebrityIds = recs.stream().map(Recommendation::getCelebrityId).distinct().toList();
        Map<Long, Celebrity> celebrityMap = celebrityService.listByIds(celebrityIds).stream()
                .collect(Collectors.toMap(Celebrity::getCelebrityId, c -> c));


        List<RecommendationVO> voList = new ArrayList<>();
        for (Recommendation rec : recs) {
            RecommendationVO vo = new RecommendationVO();
            vo.setRecordId(rec.getRecordId());
            vo.setSourceDescription(rec.getSourceDescription());
            vo.setEvidenceSummary(rec.getEvidenceSummary());
            vo.setReliability(rec.getReliability());
            vo.setEvidenceUrl(rec.getEvidenceUrl());
            vo.setBriefOverview(rec.getBriefOverview());

            // 填充图书基础信息
            vo.setBookId(book.getBookId());
            vo.setBookChineseName(book.getChineseName());
            vo.setBookEnglishName(book.getEnglishName());
            vo.setBookCoverUrl(book.getCoverUrl());
            vo.setBookOverview(book.getOverview());
            vo.setAuthorChineseName(authorName);

            // 填充名人信息
            Celebrity celebrity = celebrityMap.get(rec.getCelebrityId());
            if (celebrity != null) {
                vo.setCelebrityId(celebrity.getCelebrityId());
                vo.setCelebrityChineseName(celebrity.getChineseName());
                vo.setCelebrityEnglishName(celebrity.getEnglishName());
                vo.setCelebrityGroupName(celebrity.getGroupName());
                vo.setCelebrityAvatarUrl(celebrity.getAvatarUrl());
            }

            voList.add(vo);
        }


        return Result.success(voList);
    }

    /**
     * 获取长青经典专区图书列表
     */
    @GetMapping("/classics")
    public Result<List<ClassicBookVO>> getClassicBooks() {
        return Result.success(bookService.getClassicBooks());
    }
}
