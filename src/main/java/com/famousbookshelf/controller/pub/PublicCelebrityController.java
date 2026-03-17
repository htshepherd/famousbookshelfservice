package com.famousbookshelf.controller.pub;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.famousbookshelf.common.Result;
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

/**
 * C 端公开接口 — 名人详情
 */
@RestController
@RequestMapping("/api/public/celebrities")
@RequiredArgsConstructor
public class PublicCelebrityController {

    private final CelebrityService celebrityService;
    private final RecommendationService recommendationService;
    private final BookService bookService;
    private final AuthorService authorService;

    /**
     * 名人详情
     */
    @GetMapping("/{id}")
    public Result<Celebrity> getCelebrityDetail(@PathVariable("id") Long id) {
        Celebrity celebrity = celebrityService.getById(id);
        if (celebrity == null) {
            return Result.error(404, "名人不存在");
        }
        return Result.success(celebrity);
    }

    /**
     * 该名人推荐过的所有图书
     */
    @GetMapping("/{id}/recommendations")
    public Result<List<RecommendationVO>> getCelebrityRecommendations(@PathVariable("id") Long celebrityId) {
        List<Recommendation> recs = recommendationService.list(
                new LambdaQueryWrapper<Recommendation>()
                        .eq(Recommendation::getCelebrityId, celebrityId));

        List<RecommendationVO> voList = new ArrayList<>();
        for (Recommendation rec : recs) {
            RecommendationVO vo = new RecommendationVO();
            vo.setRecordId(rec.getRecordId());
            vo.setSourceDescription(rec.getSourceDescription());
            vo.setEvidenceSummary(rec.getEvidenceSummary());
            vo.setReliability(rec.getReliability());
            vo.setEvidenceUrl(rec.getEvidenceUrl());
            vo.setBriefOverview(rec.getBriefOverview());
            vo.setCelebrityId(rec.getCelebrityId());

            // 填充图书信息
            Book book = bookService.getById(rec.getBookId());
            if (book != null) {
                vo.setBookId(book.getBookId());
                vo.setBookChineseName(book.getChineseName());
                vo.setBookEnglishName(book.getEnglishName());
                vo.setBookCoverUrl(book.getCoverUrl());
                vo.setBookOverview(book.getOverview());

                // 填充作者信息
                Author author = authorService.getById(book.getAuthorId());
                if (author != null) {
                    vo.setAuthorChineseName(author.getChineseName());
                }
            }

            voList.add(vo);
        }

        return Result.success(voList);
    }

    /**
     * 获取全部名人列表 (按 ID 顺序排序)
     */
    @GetMapping("/all")
    public Result<List<Celebrity>> getAllCelebrities() {
        return Result.success(celebrityService.list(
                new LambdaQueryWrapper<Celebrity>()
                        .orderByAsc(Celebrity::getCelebrityId)));
    }
}
