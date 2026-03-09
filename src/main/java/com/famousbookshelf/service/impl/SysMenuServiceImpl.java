package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.entity.SysMenu;
import com.famousbookshelf.mapper.SysMenuMapper;
import com.famousbookshelf.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return baseMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<String> getPermsByUserId(Long userId) {
        return baseMapper.selectPermsByUserId(userId);
    }
}
