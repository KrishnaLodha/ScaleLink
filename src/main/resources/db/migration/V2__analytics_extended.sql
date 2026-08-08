ALTER TABLE analytics ADD COLUMN operating_system VARCHAR(50);
ALTER TABLE analytics ADD COLUMN ip_hash VARCHAR(64);

CREATE INDEX idx_analytics_url_id_country ON analytics (url_id, country);
CREATE INDEX idx_analytics_url_id_browser ON analytics (url_id, browser);
CREATE INDEX idx_analytics_url_id_device ON analytics (url_id, device);
