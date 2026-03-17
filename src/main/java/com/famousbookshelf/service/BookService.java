package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.dto.BookSearchResultVO;
import com.famousbookshelf.dto.ClassicBookVO;
import com.famousbookshelf.entity.Book;

import java.util.List;

/**
 * 图书 Service 接口
 */
public interface BookService extends IService<Book> {

    /**
     * 根据中文名、英文名、作者ID、封面URL和概述查找或创建图书。
     * 如果图书已存在，则返回现有图书；否则，创建新图书并返回。
     *
     * @param chineseName 中文名
     * @param englishName 英文名
     * @param authorId    作者ID
     * @param coverUrl    封面 URL（可为空）
     * @param overview    简要概述（可为空）
     * @return 已存在或新建的 Book
     */
    Book findOrCreate(String chineseName, String englishName, Long authorId, String coverUrl, String overview);

    /**
     * 全局搜索图书，并返回包含作者名、推荐人列表的 VO
     *
     * @param keyword 搜索关键字
     * @return 增强版图书列表
     */
    List<BookSearchResultVO> searchBooksEnriched(String keyword);

    /**
     * 后台管理分页查询，包含作者姓名
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 分页结果
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminBookVO> searchAdminPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.famousbookshelf.dto.AdminBookVO> page,
            String keyword);

    /**
     * 获取长青经典图书列表（按推荐次数排序）
     */
    List<ClassicBookVO> getClassicBooks();
}
