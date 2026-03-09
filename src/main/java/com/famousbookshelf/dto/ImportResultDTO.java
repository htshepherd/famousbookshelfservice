package com.famousbookshelf.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入结果 DTO
 */
@Data
public class ImportResultDTO {

    /** 解析总行数 */
    private int totalCount;

    /** 成功行数 */
    private int successCount;

    /** 失败行数 */
    private int failCount;

    /** 错误详情列表 (行号 + 错误原因) */
    private List<String> errors = new ArrayList<>();

    public void addError(int rowIndex, String message) {
        this.failCount++;
        this.errors.add("第 " + rowIndex + " 行：" + message);
    }

    public void incrementSuccess() {
        this.successCount++;
    }
}
