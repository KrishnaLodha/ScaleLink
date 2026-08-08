ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

CREATE INDEX IF NOT EXISTS idx_urls_created_at ON urls (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_urls_click_count ON urls (click_count DESC);
CREATE INDEX IF NOT EXISTS idx_urls_user_created ON urls (user_id, created_at DESC);
