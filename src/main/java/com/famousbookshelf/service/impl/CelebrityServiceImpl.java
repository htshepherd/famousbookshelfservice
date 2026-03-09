package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.entity.Celebrity;
import com.famousbookshelf.mapper.CelebrityMapper;
import com.famousbookshelf.service.CelebrityService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 名人 Service 实现
 */
@Service
public class CelebrityServiceImpl extends ServiceImpl<CelebrityMapper, Celebrity> implements CelebrityService {

    @Override
    public Celebrity findOrCreate(String chineseName, String englishName, String groupName, String avatarUrl) {
        String trimCn = StringUtils.hasText(chineseName) ? chineseName.trim() : null;
        String trimEn = StringUtils.hasText(englishName) ? englishName.trim() : null;
        String trimGroup = StringUtils.hasText(groupName) ? groupName.trim() : null;
        String trimAvatar = StringUtils.hasText(avatarUrl) ? avatarUrl.trim() : null;

        // 根据中英文名查找
        LambdaQueryWrapper<Celebrity> wrapper = new LambdaQueryWrapper<>();
        if (trimCn != null) {
            wrapper.eq(Celebrity::getChineseName, trimCn);
        }
        if (trimEn != null) {
            wrapper.eq(Celebrity::getEnglishName, trimEn);
        }

        Celebrity existing = this.getOne(wrapper, false);
        if (existing != null) {
            // 存在 → 选择性更新 avatar_url 和 group_name
            boolean needUpdate = false;
            if (trimAvatar != null && !StringUtils.hasText(existing.getAvatarUrl())) {
                existing.setAvatarUrl(trimAvatar);
                needUpdate = true;
            }
            if (trimGroup != null && !StringUtils.hasText(existing.getGroupName())) {
                existing.setGroupName(trimGroup);
                needUpdate = true;
            }
            if (needUpdate) {
                this.updateById(existing);
            }
            return existing;
        }

        // 不存在，新建名人
        Celebrity celebrity = new Celebrity();
        celebrity.setChineseName(trimCn);
        celebrity.setEnglishName(trimEn);
        celebrity.setGroupName(trimGroup);
        celebrity.setAvatarUrl(trimAvatar);
        this.save(celebrity);
        return celebrity;
    }
}
