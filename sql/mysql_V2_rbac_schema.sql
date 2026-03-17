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

-- ================================================
-- 初始化菜单树
-- ================================================
INSERT IGNORE INTO sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, sort_order, menu_type) VALUES
-- 一级目录：内容管理
(1,  0, '内容管理', '/admin',        NULL,                    NULL,                    'Folder',     1, 'M'),
(2,  1, '图书管理', 'books',         'admin/AdminBooks',      'content:book:list',     'Reading',    1, 'C'),
(3,  1, '作者管理', 'authors',       'admin/AdminAuthors',    'content:author:list',   'User',       2, 'C'),
(4,  1, '名人管理', 'celebrities',   'admin/AdminCelebrity',  'content:celebrity:list','Star',       3, 'C'),
(5,  1, '推荐记录', 'recommendations','admin/AdminRecommendations','content:rec:list', 'Promotion',  4, 'C'),

-- 一级目录：系统管理
(10, 0, '系统管理', '/admin',        NULL,                    NULL,                    'Setting',    2, 'M'),
(11, 10,'用户管理', 'users',         'admin/AdminUsers',      'system:user:list',      'Avatar',     1, 'C'),
(12, 10,'角色管理', 'roles',         'admin/AdminRoles',      'system:role:list',      'Key',        2, 'C'),
(13, 10,'菜单管理', 'menus',         'admin/AdminMenus',      'system:menu:list',      'Menu',       3, 'C'),

-- 一级目录：工具与数据
(20, 0, '工具与数据','/admin',       NULL,                    NULL,                    'Tools',      3, 'M'),
(21, 20,'批量导入', 'import',        'admin/AdminImport',     'tool:import:do',        'Upload',     1, 'C'),

-- 按钮权限：图书
(30, 2, '新增图书', NULL, NULL, 'content:book:add',    NULL, 1, 'F'),
(31, 2, '编辑图书', NULL, NULL, 'content:book:edit',   NULL, 2, 'F'),
(32, 2, '删除图书', NULL, NULL, 'content:book:delete', NULL, 3, 'F'),

-- 按钮权限：作者
(33, 3, '新增作者', NULL, NULL, 'content:author:add',    NULL, 1, 'F'),
(34, 3, '编辑作者', NULL, NULL, 'content:author:edit',   NULL, 2, 'F'),
(35, 3, '删除作者', NULL, NULL, 'content:author:delete', NULL, 3, 'F'),

-- 按钮权限：名人
(36, 4, '新增名人', NULL, NULL, 'content:celebrity:add',    NULL, 1, 'F'),
(37, 4, '编辑名人', NULL, NULL, 'content:celebrity:edit',   NULL, 2, 'F'),
(38, 4, '删除名人', NULL, NULL, 'content:celebrity:delete', NULL, 3, 'F'),

-- 按钮权限：用户
(40, 11,'新增用户', NULL, NULL, 'system:user:add',    NULL, 1, 'F'),
(41, 11,'编辑用户', NULL, NULL, 'system:user:edit',   NULL, 2, 'F'),
(42, 11,'删除用户', NULL, NULL, 'system:user:delete', NULL, 3, 'F'),

-- 按钮权限：角色
(43, 12,'新增角色', NULL, NULL, 'system:role:add',    NULL, 1, 'F'),
(44, 12,'编辑角色', NULL, NULL, 'system:role:edit',   NULL, 2, 'F'),
(45, 12,'删除角色', NULL, NULL, 'system:role:delete', NULL, 3, 'F'),

-- 按钮权限：菜单
(46, 13,'新增菜单', NULL, NULL, 'system:menu:add',    NULL, 1, 'F'),
(47, 13,'编辑菜单', NULL, NULL, 'system:menu:edit',   NULL, 2, 'F'),
(48, 13,'删除菜单', NULL, NULL, 'system:menu:delete', NULL, 3, 'F');

-- ================================================
-- 初始化角色
-- ================================================
INSERT IGNORE INTO sys_role (role_id, role_name, role_code, description) VALUES
(1, '超级管理员', 'super_admin',  '拥有系统最高权限，可操作所有模块'),
(2, '普通管理员', 'common_admin', '负责日常内容运营，无系统管理权限');

-- ================================================
-- 超级管理员绑定所有菜单权限
-- ================================================
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE is_deleted = 0;

-- ================================================
-- 初始化 admin 用户 (密码: admin123)
-- ================================================
INSERT IGNORE INTO sys_user (user_id, username, password, nickname)
VALUES (1, 'admin', '$2a$10$.2D9w4d0WA1sYPGSGBzp7uX8UO8iyTUEynUzzAIi2I2/2Ugy9XOKK', '超级管理员');

-- admin 绑定超级管理员角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (1, 1);
