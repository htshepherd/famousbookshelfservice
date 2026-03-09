package com.famousbookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.famousbookshelf.entity.Author;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作者 Mapper 接口
 */
@Mapper
public interface AuthorMapper extends BaseMapper<Author> {
}
