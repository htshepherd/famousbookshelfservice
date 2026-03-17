package com.famousbookshelf.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ABTestService {

    /**
     * 根据用户标识判断是否属于某个实验组
     * @param userId 用户 ID 或 匿名标识 (Cookie/IP)
     * @param experimentName 实验名称
     * @param percentage 实验组占比 (0-100)
     * @return true if in experimental group
     */
    public boolean isInExperimentalGroup(String userId, String experimentName, int percentage) {
        if (userId == null) {
            userId = UUID.randomUUID().toString();
        }
        
        // 使用简单的哈希算法确保一致性
        String hashKey = userId + ":" + experimentName;
        int hash = Math.abs(hashKey.hashCode());
        return (hash % 100) < percentage;
    }
}
