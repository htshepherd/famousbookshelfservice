-- ============================================================
-- 名人书架 (Famous Bookshelf) — PostgreSQL DDL
-- 适用于 Supabase (PostgreSQL 15+)
-- ============================================================

-- ==================== 1. 作者表 (Author) ====================
CREATE TABLE IF NOT EXISTS author (
    author_id    BIGSERIAL    PRIMARY KEY,
    chinese_name VARCHAR(200),
    english_name VARCHAR(200),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE  author              IS '作者表';
COMMENT ON COLUMN author.chinese_name IS '中文名';
COMMENT ON COLUMN author.english_name IS '英文名';
COMMENT ON COLUMN author.is_deleted   IS '逻辑删除标识';

-- ==================== 2. 图书表 (Book) ======================
CREATE TABLE IF NOT EXISTS book (
    book_id      BIGSERIAL    PRIMARY KEY,
    chinese_name VARCHAR(500),
    english_name VARCHAR(500),
    author_id    BIGINT       NOT NULL,
    overview     TEXT,
    cover_url    VARCHAR(1000),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_book_author
        FOREIGN KEY (author_id) REFERENCES author (author_id)
);

COMMENT ON TABLE  book              IS '图书表';
COMMENT ON COLUMN book.chinese_name IS '中文书名';
COMMENT ON COLUMN book.english_name IS '英文书名';
COMMENT ON COLUMN book.author_id    IS '作者外键';
COMMENT ON COLUMN book.overview     IS '图书概况/概述';
COMMENT ON COLUMN book.cover_url    IS '封面图网络地址';

-- ==================== 3. 名人表 (Celebrity) =================
CREATE TABLE IF NOT EXISTS celebrity (
    celebrity_id BIGSERIAL    PRIMARY KEY,
    chinese_name VARCHAR(200),
    english_name VARCHAR(200),
    group_name   VARCHAR(200),
    avatar_url   VARCHAR(1000),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE  celebrity              IS '名人表';
COMMENT ON COLUMN celebrity.chinese_name IS '中文名';
COMMENT ON COLUMN celebrity.english_name IS '英文名';
COMMENT ON COLUMN celebrity.group_name   IS '名人分组';
COMMENT ON COLUMN celebrity.avatar_url   IS '头像网络地址';

-- ============ 4. 图书推荐记录表 (Recommendation) ============
CREATE TABLE IF NOT EXISTS recommendation (
    record_id          BIGSERIAL     PRIMARY KEY,
    book_id            BIGINT        NOT NULL,
    celebrity_id       BIGINT        NOT NULL,
    source_description VARCHAR(1000),
    evidence_summary   TEXT,
    reliability        VARCHAR(100),
    evidence_url       VARCHAR(1000),
    brief_overview     TEXT,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    is_deleted         BOOLEAN       NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_recommendation_book
        FOREIGN KEY (book_id) REFERENCES book (book_id),
    CONSTRAINT fk_recommendation_celebrity
        FOREIGN KEY (celebrity_id) REFERENCES celebrity (celebrity_id)
);

COMMENT ON TABLE  recommendation                    IS '图书推荐记录表';
COMMENT ON COLUMN recommendation.source_description IS '出处说明';
COMMENT ON COLUMN recommendation.evidence_summary   IS '证据摘要';
COMMENT ON COLUMN recommendation.reliability        IS '可靠度';
COMMENT ON COLUMN recommendation.evidence_url       IS '证据链接';
COMMENT ON COLUMN recommendation.brief_overview     IS '简要概述';

-- ======================== 5. 索引 ===========================

-- Author: 按姓名快速查找（用于 Upsert 和搜索）
CREATE INDEX idx_author_chinese_name ON author (chinese_name) WHERE is_deleted = FALSE;
CREATE INDEX idx_author_english_name ON author (english_name) WHERE is_deleted = FALSE;

-- Book: 按作者查书 & 按书名快速查找
CREATE INDEX idx_book_author_id    ON book (author_id)    WHERE is_deleted = FALSE;
CREATE INDEX idx_book_chinese_name ON book (chinese_name) WHERE is_deleted = FALSE;
CREATE INDEX idx_book_english_name ON book (english_name) WHERE is_deleted = FALSE;

-- Celebrity: 按姓名快速查找 & 按分组筛选
CREATE INDEX idx_celebrity_chinese_name ON celebrity (chinese_name) WHERE is_deleted = FALSE;
CREATE INDEX idx_celebrity_english_name ON celebrity (english_name) WHERE is_deleted = FALSE;
CREATE INDEX idx_celebrity_group_name   ON celebrity (group_name)   WHERE is_deleted = FALSE;

-- Recommendation: 按书/名人查推荐 & 唯一性查重
CREATE INDEX idx_recommendation_book_id      ON recommendation (book_id)      WHERE is_deleted = FALSE;
CREATE INDEX idx_recommendation_celebrity_id ON recommendation (celebrity_id) WHERE is_deleted = FALSE;
-- 用于 Excel 导入查重：book_id + celebrity_id + source_description
CREATE INDEX idx_recommendation_dedup
    ON recommendation (book_id, celebrity_id, source_description)
    WHERE is_deleted = FALSE;

-- =============== 6. updated_at 自动更新触发器 ===============
CREATE OR REPLACE FUNCTION fn_update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_author_updated_at
    BEFORE UPDATE ON author
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_book_updated_at
    BEFORE UPDATE ON book
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_celebrity_updated_at
    BEFORE UPDATE ON celebrity
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_recommendation_updated_at
    BEFORE UPDATE ON recommendation
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();
