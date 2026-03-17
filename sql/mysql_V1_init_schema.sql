-- 名人书架 MySQL DDL

CREATE TABLE IF NOT EXISTS author (
    author_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '作者ID',
    chinese_name VARCHAR(200) COMMENT '中文名',
    english_name VARCHAR(200) COMMENT '英文名',
    created_at   DATETIME     NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    is_deleted   TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) COMMENT='作者表';

CREATE TABLE IF NOT EXISTS book (
    book_id      BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    chinese_name VARCHAR(500)  COMMENT '中文书名',
    english_name VARCHAR(500)  COMMENT '英文书名',
    author_id    BIGINT        NOT NULL COMMENT '作者外键',
    overview     TEXT          COMMENT '图书概述',
    cover_url    VARCHAR(1000) COMMENT '封面图地址',
    created_at   DATETIME      NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at   DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    is_deleted   TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES author(author_id)
) COMMENT='图书表';

CREATE TABLE IF NOT EXISTS celebrity (
    celebrity_id BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '名人ID',
    chinese_name VARCHAR(200)  COMMENT '中文名',
    english_name VARCHAR(200)  COMMENT '英文名',
    group_name   VARCHAR(200)  COMMENT '分组',
    avatar_url   VARCHAR(1000) COMMENT '头像地址',
    created_at   DATETIME      NOT NULL DEFAULT NOW(),
    updated_at   DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    is_deleted   TINYINT(1)    NOT NULL DEFAULT 0
) COMMENT='名人表';

CREATE TABLE IF NOT EXISTS recommendation (
    record_id          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_id            BIGINT        NOT NULL,
    celebrity_id       BIGINT        NOT NULL,
    source_description VARCHAR(1000) COMMENT '出处说明',
    evidence_summary   TEXT          COMMENT '证据摘要',
    reliability        VARCHAR(100)  COMMENT '可靠度',
    evidence_url       VARCHAR(1000) COMMENT '证据链接',
    brief_overview     TEXT          COMMENT '简要概述',
    created_at         DATETIME      NOT NULL DEFAULT NOW(),
    updated_at         DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    is_deleted         TINYINT(1)    NOT NULL DEFAULT 0,
    CONSTRAINT fk_rec_book      FOREIGN KEY (book_id)      REFERENCES book(book_id),
    CONSTRAINT fk_rec_celebrity FOREIGN KEY (celebrity_id) REFERENCES celebrity(celebrity_id)
) COMMENT='图书推荐记录表';

-- 索引（MySQL 不支持条件索引，直接建普通索引）
CREATE INDEX idx_author_chinese_name ON author(chinese_name);
CREATE INDEX idx_author_english_name ON author(english_name);
CREATE INDEX idx_book_author_id      ON book(author_id);
CREATE INDEX idx_book_chinese_name   ON book(chinese_name);
CREATE INDEX idx_book_english_name   ON book(english_name);
CREATE INDEX idx_celebrity_chinese   ON celebrity(chinese_name);
CREATE INDEX idx_celebrity_english   ON celebrity(english_name);
CREATE INDEX idx_celebrity_group     ON celebrity(group_name);
CREATE INDEX idx_rec_book_id         ON recommendation(book_id);
CREATE INDEX idx_rec_celebrity_id    ON recommendation(celebrity_id);
