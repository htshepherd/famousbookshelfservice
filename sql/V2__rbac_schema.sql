-- ============================================================
-- RBAC 权限系统 — PostgreSQL DDL
-- ============================================================

-- ==================== 0. 用户表 (sys_user) ====================
CREATE TABLE IF NOT EXISTS sys_user (
    user_id       BIGSERIAL    PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    nickname      VARCHAR(100),
    email         VARCHAR(255),
    status        SMALLINT     DEFAULT 1, -- 1: 正常, 0: 停用
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE  sys_user              IS '系统用户表';
COMMENT ON COLUMN sys_user.username     IS '登录用户名';
COMMENT ON COLUMN sys_user.password     IS '加密密码';
COMMENT ON COLUMN sys_user.nickname     IS '用户昵称';
COMMENT ON COLUMN sys_user.status       IS '账号状态 (1正常 0禁用)';

-- ==================== 1. 角色表 (sys_role) ====================
CREATE TABLE IF NOT EXISTS sys_role (
    role_id       BIGSERIAL    PRIMARY KEY,
    role_name     VARCHAR(100) NOT NULL,
    role_code     VARCHAR(100) NOT NULL UNIQUE,
    description   VARCHAR(255),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE  sys_role              IS '角色表';
COMMENT ON COLUMN sys_role.role_name    IS '角色名称';
COMMENT ON COLUMN sys_role.role_code    IS '角色编码(权限标识)';

-- ==================== 2. 权限/菜单表 (sys_menu) ====================
CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id       BIGSERIAL    PRIMARY KEY,
    parent_id     BIGINT       DEFAULT 0,
    menu_name     VARCHAR(100) NOT NULL,
    path          VARCHAR(255),
    component     VARCHAR(255),
    perms         VARCHAR(255),
    icon          VARCHAR(100),
    sort_order    INT          DEFAULT 0,
    menu_type     CHAR(1)      NOT NULL, -- M:目录, C:菜单, F:按钮
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE  sys_menu              IS '菜单权限表';
COMMENT ON COLUMN sys_menu.menu_type    IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.perms        IS '后端接口权限标识（如 system:user:add）';

-- ==================== 3. 用户角色关联表 (sys_user_role) ====================
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id       BIGINT       NOT NULL,
    role_id       BIGINT       NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- ==================== 4. 角色菜单关联表 (sys_role_menu) ====================
CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id       BIGINT       NOT NULL,
    menu_id       BIGINT       NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

-- ==================== 5. 初始化基础数据 ====================

-- 插入默认管理员: admin / admin123
INSERT INTO sys_user (username, password, nickname) 
VALUES ('admin', '$2a$10$.2D9w4d0WA1sYPGSGBzp7uX8UO8iyTUEynUzzAIi2I2/2Ugy9XOKK', '超级管理员')
ON CONFLICT (username) DO NOTHING;

-- 插入默认角色
INSERT INTO sys_role (role_name, role_code, description) 
VALUES 
('超级管理员', 'super_admin', '拥有系统最高权限'),
('普通管理员', 'common_admin', '负责日常内容运营')
ON CONFLICT (role_code) DO NOTHING;

-- 关联 admin 用户到超级管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.role_code = 'super_admin'
ON CONFLICT DO NOTHING;

-- 初始化基础菜单
-- 分类 200+: 内容管理, 300+: 系统管理, 400+: 工具与数据
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, sort_order, menu_type)
VALUES 
-- 内容管理根目录
(200, 0, '内容管理', NULL, NULL, NULL, 'Collection', 1, 'M'),
(201, 200, '图书管理', '/admin/books', 'admin/AdminBooks', 'content:book:list', 'Reading', 1, 'C'),
(202, 201, '图书新增', NULL, NULL, 'content:book:add', NULL, 1, 'F'),
(203, 201, '图书修改', NULL, NULL, 'content:book:update', NULL, 2, 'F'),
(204, 201, '图书删除', NULL, NULL, 'content:book:delete', NULL, 3, 'F'),

(211, 200, '作者管理', '/admin/authors', 'admin/AdminAuthors', 'content:author:list', 'Edit', 2, 'C'),
(212, 211, '作者新增', NULL, NULL, 'content:author:add', NULL, 1, 'F'),
(213, 211, '作者修改', NULL, NULL, 'content:author:update', NULL, 2, 'F'),
(214, 211, '作者删除', NULL, NULL, 'content:author:delete', NULL, 3, 'F'),

(221, 200, '名人管理', '/admin/celebrities', 'admin/AdminCelebrities', 'content:celebrity:list', 'Star', 3, 'C'),
(222, 221, '名人新增', NULL, NULL, 'content:celebrity:add', NULL, 1, 'F'),
(223, 221, '名人修改', NULL, NULL, 'content:celebrity:update', NULL, 2, 'F'),
(224, 221, '名人删除', NULL, NULL, 'content:celebrity:delete', NULL, 3, 'F'),

(231, 200, '推荐记录', '/admin/recommendations', 'admin/AdminRecommendations', 'content:recommendation:list', 'User', 4, 'C'),
(232, 231, '记录新增', NULL, NULL, 'content:recommendation:add', NULL, 1, 'F'),
(233, 231, '记录修改', NULL, NULL, 'content:recommendation:update', NULL, 2, 'F'),
(234, 231, '记录删除', NULL, NULL, 'content:recommendation:delete', NULL, 3, 'F'),

-- 数据工具根目录
(400, 0, '工具与数据', NULL, NULL, NULL, 'Tools', 3, 'M'),
(401, 400, '批量导入', '/admin/import', 'admin/AdminImport', 'tool:import:all', 'Upload', 1, 'C'),

-- 系统管理根目录
(1, 0, '系统管理', NULL, NULL, NULL, 'Setting', 100, 'M'),
(2, 1, '用户管理', '/admin/users', 'system/user/index', 'system:user:list', 'User', 1, 'C'),
(5, 2, '新增用户', NULL, NULL, 'system:user:add', NULL, 1, 'F'),
(6, 2, '修改用户', NULL, NULL, 'system:user:update', NULL, 2, 'F'),
(7, 2, '删除用户', NULL, NULL, 'system:user:delete', NULL, 3, 'F'),
(11, 2, '分配角色', NULL, NULL, 'system:user:assign', NULL, 4, 'F'),

(3, 1, '角色管理', '/admin/roles', 'system/role/index', 'system:role:list', 'Peoples', 2, 'C'),
(8, 3, '新增角色', NULL, NULL, 'system:role:add', NULL, 1, 'F'),
(9, 3, '修改角色', NULL, NULL, 'system:role:update', NULL, 2, 'F'),
(10, 3, '删除角色', NULL, NULL, 'system:role:delete', NULL, 3, 'F'),
(12, 3, '分配权限', NULL, NULL, 'system:role:assign', NULL, 4, 'F'),

(4, 1, '菜单管理', '/admin/menus', 'system/menu/index', 'system:menu:list', 'List', 3, 'C'),
(13, 4, '新增菜单', NULL, NULL, 'system:menu:add', NULL, 1, 'F'),
(14, 4, '修改菜单', NULL, NULL, 'system:menu:update', NULL, 2, 'F'),
(15, 4, '删除菜单', NULL, NULL, 'system:menu:delete', NULL, 3, 'F')

ON CONFLICT (menu_id) DO NOTHING;

-- 给 super_admin 角色分配所有菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT (SELECT role_id FROM sys_role WHERE role_code = 'super_admin'), menu_id FROM sys_menu
ON CONFLICT DO NOTHING;

-- 给 common_admin 角色分配内容管理和工具数据菜单（不包含系统管理）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT (SELECT role_id FROM sys_role WHERE role_code = 'common_admin'), menu_id 
FROM sys_menu 
WHERE menu_id >= 200 AND menu_id < 500
ON CONFLICT DO NOTHING;

-- ==================== 6. 自动更新时间戳触发器 ====================
-- (fn_update_timestamp 已在 V1__init_schema.sql 中定义)

CREATE TRIGGER trg_sys_user_updated_at
    BEFORE UPDATE ON sys_user
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_sys_role_updated_at
    BEFORE UPDATE ON sys_role
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_sys_menu_updated_at
    BEFORE UPDATE ON sys_menu
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();
