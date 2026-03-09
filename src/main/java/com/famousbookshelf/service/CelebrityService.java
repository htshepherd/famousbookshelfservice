package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.entity.Celebrity;

/**
 * 名人 Service 接口
 */
public interface CelebrityService extends IService<Celebrity> {

    /**
     * 根据中英文名查找名人，不存在则新建（带入分组和头像），存在则选择性更新
     *
     * @param chineseName 中文名（可为空）
     * @param englishName 英文名（可为空）
     * @param groupName   分组名
     * @param avatarUrl   头像 URL
     * @return 已存在或新建的 Celebrity
     */
    Celebrity findOrCreate(String chineseName, String englishName, String groupName, String avatarUrl);
}
