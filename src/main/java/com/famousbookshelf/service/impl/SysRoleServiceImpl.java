package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.entity.SysRole;
import com.famousbookshelf.entity.SysRoleMenu;
import com.famousbookshelf.mapper.SysRoleMapper;
import com.famousbookshelf.mapper.SysRoleMenuMapper;
import com.famousbookshelf.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        // Remove existing mappings
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));

        // Add new mappings
        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> new SysRoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            // Normally we'd use a batch insert service, but for simplicity here we just
            // loop or use mapper if available
            // Since we don't have a service for SysRoleMenu, we use its mapper
            for (SysRoleMenu rm : roleMenus) {
                roleMenuMapper.insert(rm);
            }
        }
    }
}
