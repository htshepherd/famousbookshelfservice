package com.famousbookshelf.controller.pub;

import com.alibaba.excel.EasyExcel;
import com.famousbookshelf.dto.RecommendationImportDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 开放端 — 下载模板接口
 */
@Slf4j
@RestController
@RequestMapping("/api/public")
public class PublicTemplateController {

    /**
     * 下载导入推荐记录的 Excel 模板
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 设置响应头，告诉浏览器下载文件
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            // 防止中文文件名乱码
            String fileName = URLEncoder.encode("推荐记录导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用 EasyExcel 根据 DTO 动态生成一个没有数据的模板
            EasyExcel.write(response.getOutputStream(), RecommendationImportDTO.class)
                    .sheet("模板")
                    .doWrite(new ArrayList<>());

        } catch (IOException e) {
            log.error("下载模板失败", e);
            response.setStatus(500);
        }
    }
}
