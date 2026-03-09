package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.entity.Author;

/**
 * 作者 Service 接口
 */
public interface AuthorService extends IService<Author> {

    /**
     * 根据中英文名查找作者（trim 后匹配），不存在则新建
     *
     * @param chineseName 中文名（可为空）
     * @param englishName 英文名（可为空）
     * @return 已存在或新建的 Author
     */
    Author findOrCreate(String chineseName, String englishName);
}
