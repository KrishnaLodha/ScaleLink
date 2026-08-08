CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users (email);

CREATE TABLE urls (
    id              BIGSERIAL PRIMARY KEY,
    original_url    TEXT         NOT NULL,
    short_code      VARCHAR(10)  NOT NULL UNIQUE,
    custom_alias    VARCHAR(50)  UNIQUE,
    user_id         BIGINT       REFERENCES users(id) ON DELETE SET NULL,
    click_count     BIGINT       NOT NULL DEFAULT 0,
    expiration_date TIMESTAMPTZ,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_short_code_format CHECK (short_code ~ '^[a-zA-Z0-9_-]+$'),
    CONSTRAINT chk_custom_alias_format CHECK (
        custom_alias IS NULL OR custom_alias ~ '^[a-zA-Z0-9_-]+$'
    )
);

CREATE UNIQUE INDEX idx_urls_short_code ON urls (short_code);
CREATE UNIQUE INDEX idx_urls_custom_alias ON urls (custom_alias) WHERE custom_alias IS NOT NULL;
CREATE INDEX idx_urls_user_id ON urls (user_id);
CREATE INDEX idx_urls_expiration ON urls (expiration_date) WHERE expiration_date IS NOT NULL;

CREATE TABLE analytics (
    id          BIGSERIAL PRIMARY KEY,
    url_id      BIGINT       NOT NULL REFERENCES urls(id) ON DELETE CASCADE,
    timestamp   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    country     VARCHAR(2),
    browser     VARCHAR(50),
    device      VARCHAR(50),
    referrer    TEXT
);

CREATE INDEX idx_analytics_url_id_timestamp ON analytics (url_id, timestamp DESC);
CREATE INDEX idx_analytics_timestamp ON analytics (timestamp);
