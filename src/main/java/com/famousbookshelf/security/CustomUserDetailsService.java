package com.famousbookshelf.security;

import com.famousbookshelf.entity.User;
import com.famousbookshelf.service.UserService;
import com.famousbookshelf.service.SysRoleService;
import com.famousbookshelf.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final SysRoleService roleService;
    private final SysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UsernameNotFoundException("该账号已被禁用");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Load roles
        roleService.getRolesByUserId(user.getUserId()).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
        });

        // Load permissions
        menuService.getPermsByUserId(user.getUserId()).forEach(perm -> {
            authorities.add(new SimpleGrantedAuthority(perm));
        });

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
