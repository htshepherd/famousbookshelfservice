package com.famousbookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.famousbookshelf.entity.Celebrity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 名人 Mapper 接口
 */
@Mapper
public interface CelebrityMapper extends BaseMapper<Celebrity> {
}
