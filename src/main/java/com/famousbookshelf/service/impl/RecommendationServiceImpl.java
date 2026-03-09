package com.famousbookshelf.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.dto.ImportResultDTO;
import com.famousbookshelf.dto.RecommendationImportDTO;
import com.famousbookshelf.entity.Author;
import com.famousbookshelf.entity.Book;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.entity.Recommendation;
import com.famousbookshelf.mapper.RecommendationMapper;
import com.famousbookshelf.service.AuthorService;
import com.famousbookshelf.service.BookService;
import com.famousbookshelf.service.CelebrityService;
import com.famousbookshelf.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 推荐记录 Service 实现
 * 核心：Excel 导入与级联 Upsert（单行隔离容错）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl
        extends ServiceImpl<RecommendationMapper, Recommendation>
        implements RecommendationService {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CelebrityService celebrityService;

    @Override
    public ImportResultDTO importFromExcel(MultipartFile file) {
        ImportResultDTO result = new ImportResultDTO();

        try {
            EasyExcel.read(file.getInputStream(), RecommendationImportDTO.class,
                    new RecommendationReadListener(result))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            log.error("读取 Excel 文件失败", e);
            result.addError(0, "文件读取失败：" + e.getMessage());
        }

        result.setTotalCount(result.getSuccessCount() + result.getFailCount());
        return result;
    }

    // ======================================================================
    // EasyExcel 逐行读取监听器（内部类，直接访问外部 Service）
    // ======================================================================
    private class RecommendationReadListener implements ReadListener<RecommendationImportDTO> {

        private final ImportResultDTO result;
        /** 当前行号（EasyExcel 从 0 开始，+2 转换为 Excel 可读行号：跳过表头） */
        private int currentRow = 0;

        public RecommendationReadListener(ImportResultDTO result) {
            this.result = result;
        }

        @Override
        public void invoke(RecommendationImportDTO dto, AnalysisContext context) {
            currentRow++;
            int excelRow = currentRow + 1; // +1 因为表头占一行

            try {
                // ===== 1. 校验必填项 =====
                boolean hasBookName = StringUtils.hasText(dto.getBookChineseName())
                        || StringUtils.hasText(dto.getBookEnglishName());
                if (!hasBookName) {
                    result.addError(excelRow, "图书中英文名至少需要填写一项");
                    return;
                }

                boolean hasAuthorName = StringUtils.hasText(dto.getAuthorChineseName())
                        || StringUtils.hasText(dto.getAuthorEnglishName());
                if (!hasAuthorName) {
                    result.addError(excelRow, "作者中英文名至少需要填写一项");
                    return;
                }

                boolean hasCelebrityName = StringUtils.hasText(dto.getCelebrityChineseName())
                        || StringUtils.hasText(dto.getCelebrityEnglishName());
                if (!hasCelebrityName) {
                    result.addError(excelRow, "名人中英文名至少需要填写一项");
                    return;
                }

                if (!StringUtils.hasText(dto.getSourceDescription())) {
                    result.addError(excelRow, "出处说明不可为空");
                    return;
                }

                // ===== 2. 查/建 Author =====
                Author author = authorService.findOrCreate(
                        dto.getAuthorChineseName(),
                        dto.getAuthorEnglishName());

                // ===== 3. 查/建 Book（带入 cover_url）=====
                Book book = bookService.findOrCreate(
                        dto.getBookChineseName(),
                        dto.getBookEnglishName(),
                        author.getAuthorId(),
                        dto.getCoverUrl(),
                        dto.getBriefOverview());

                // ===== 4. 查/建 Celebrity（带入 avatar_url + group_name）=====
                Celebrity celebrity = celebrityService.findOrCreate(
                        dto.getCelebrityChineseName(),
                        dto.getCelebrityEnglishName(),
                        dto.getGroupName(),
                        dto.getAvatarUrl());

                // ===== 5. 查重并新建 Recommendation =====
                String trimSource = dto.getSourceDescription().trim();

                LambdaQueryWrapper<Recommendation> dedupWrapper = new LambdaQueryWrapper<>();
                dedupWrapper.eq(Recommendation::getBookId, book.getBookId())
                        .eq(Recommendation::getCelebrityId, celebrity.getCelebrityId())
                        .eq(Recommendation::getSourceDescription, trimSource);

                long count = count(dedupWrapper);
                if (count > 0) {
                    result.addError(excelRow, "推荐记录已存在（相同图书+名人+出处），跳过");
                    return;
                }

                Recommendation rec = new Recommendation();
                rec.setBookId(book.getBookId());
                rec.setCelebrityId(celebrity.getCelebrityId());
                rec.setSourceDescription(trimSource);
                rec.setEvidenceSummary(
                        StringUtils.hasText(dto.getEvidenceSummary()) ? dto.getEvidenceSummary().trim() : null);
                rec.setReliability(
                        StringUtils.hasText(dto.getReliability()) ? dto.getReliability().trim() : null);
                rec.setEvidenceUrl(
                        StringUtils.hasText(dto.getEvidenceUrl()) ? dto.getEvidenceUrl().trim() : null);
                rec.setBriefOverview(
                        StringUtils.hasText(dto.getBriefOverview()) ? dto.getBriefOverview().trim() : null);

                save(rec);
                result.incrementSuccess();

            } catch (Exception e) {
                // 单行异常隔离：记录错误，继续处理下一行
                log.error("第 {} 行处理失败", excelRow, e);
                result.addError(excelRow, "处理异常：" + e.getMessage());
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            log.info("Excel 全部解析完成，共 {} 行，成功 {} 行，失败 {} 行",
                    result.getTotalCount(), result.getSuccessCount(), result.getFailCount());
        }
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminRecommendationVO> searchAdminPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminRecommendationVO> page,
            String keyword) {
        return baseMapper.searchAdminPage(page, keyword);
    }
}
