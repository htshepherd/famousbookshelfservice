package com.famousbookshelf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.famousbookshelf.dto.UserVO;
import com.famousbookshelf.entity.SysUserRole;
import com.famousbookshelf.entity.User;
import com.famousbookshelf.mapper.SysRoleMapper;
import com.famousbookshelf.mapper.SysUserRoleMapper;
import com.famousbookshelf.mapper.UserMapper;
import com.famousbookshelf.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public User findByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        // Remove existing roles
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));

        // Add new roles
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                userRoleMapper.insert(new SysUserRole(userId, roleId));
            }
        }
    }

    @Override
    public Page<UserVO> getAdminPage(Page<User> page) {
        Page<User> userPage = baseMapper.selectPage(page, new LambdaQueryWrapper<User>().orderByDesc(User::getUserId));

        List<UserVO> voList = userPage.getRecords().stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            // Fetch roles for this user
            vo.setRoles(roleMapper.selectRolesByUserId(user.getUserId()));
            return vo;
        }).collect(Collectors.toList());

        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
}
