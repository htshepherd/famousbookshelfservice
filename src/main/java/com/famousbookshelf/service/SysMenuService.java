package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.entity.SysMenu;
import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getMenusByUserId(Long userId);

    List<String> getPermsByUserId(Long userId);
}
