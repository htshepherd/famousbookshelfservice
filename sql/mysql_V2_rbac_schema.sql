CREATE TABLE IF NOT EXISTS sys_user (
    user_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE COMMENT '登录用户名',
    password   VARCHAR(255) NOT NULL COMMENT '加密密码',
    nickname   VARCHAR(100) COMMENT '昵称',
    email      VARCHAR(255),
    status     TINYINT      DEFAULT 1 COMMENT '1正常 0禁用',
    created_at DATETIME     NOT NULL DEFAULT NOW(),
    updated_at DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    is_deleted TINYINT(1)   NOT NULL DEFAULT 0
) COMMENT='系统用户表';

CREATE TABLE IF NOT EXISTS sys_role (
    role_id     BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_name   VARCHAR(100) NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(100) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255),
    created_at  DATETIME     NOT NULL DEFAULT NOW(),
    updated_at  DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    is_deleted  TINYINT(1)   NOT NULL DEFAULT 0
) COMMENT='角色表';

CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    parent_id  BIGINT       DEFAULT 0,
    menu_name  VARCHAR(100) NOT NULL,
    path       VARCHAR(255),
    component  VARCHAR(255),
    perms      VARCHAR(255) COMMENT '权限标识',
    icon       VARCHAR(100),
    sort_order INT          DEFAULT 0,
    menu_type  CHAR(1)      NOT NULL COMMENT 'M目录 C菜单 F按钮',
    created_at DATETIME     NOT NULL DEFAULT NOW(),
    updated_at DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    is_deleted TINYINT(1)   NOT NULL DEFAULT 0
) COMMENT='菜单权限表';

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

-- 初始管理员 admin / admin123
INSERT IGNORE INTO sys_user (username, password, nickname)
VALUES ('admin', '$2a$10$.2D9w4d0WA1sYPGSGBzp7uX8UO8iyTUEynUzzAIi2I2/2Ugy9XOKK', '超级管理员');

INSERT IGNORE INTO sys_role (role_name, role_code, description)
VALUES ('超级管理员', 'super_admin', '拥有系统最高权限'),
       ('普通管理员', 'common_admin', '负责日常内容运营');

-- 关联角色
INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM sys_user u, sys_role r
WHERE u.username = 'admin' AND r.role_code = 'super_admin';
