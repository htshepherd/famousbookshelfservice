package com.famousbookshelf.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * EasyExcel 行数据映射 DTO
 * 与《推荐记录导入模板.xlsx》表头一一对应
 */
@Data
public class RecommendationImportDTO {

    @ExcelProperty("图书中文名")
    private String bookChineseName;

    @ExcelProperty("图书英文名")
    private String bookEnglishName;

    @ExcelProperty("封面URL")
    private String coverUrl;

    @ExcelProperty("作者中文名")
    private String authorChineseName;

    @ExcelProperty("作者英文名")
    private String authorEnglishName;

    @ExcelProperty("名人中文名")
    private String celebrityChineseName;

    @ExcelProperty("名人英文名")
    private String celebrityEnglishName;

    @ExcelProperty("头像URL")
    private String avatarUrl;

    @ExcelProperty("名人分组")
    private String groupName;

    @ExcelProperty("出处说明")
    private String sourceDescription;

    @ExcelProperty("证据摘要")
    private String evidenceSummary;

    @ExcelProperty("可靠度")
    private String reliability;

    @ExcelProperty("证据链接")
    private String evidenceUrl;

    @ExcelProperty("简要概述")
    private String briefOverview;
}
