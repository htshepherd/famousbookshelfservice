package com.famousbookshelf.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.famousbookshelf.dto.UserVO;
import com.famousbookshelf.entity.User;
import java.util.List;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    void assignRoles(Long userId, List<Long> roleIds);

    Page<UserVO> getAdminPage(Page<User> page);
}
