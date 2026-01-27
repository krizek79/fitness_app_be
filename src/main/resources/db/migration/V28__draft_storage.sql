CREATE TABLE IF NOT EXISTS draft_storage
(
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    entity_type VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    content JSONB NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    last_modified_at TIMESTAMPTZ NOT NULL,
    last_modified_by VARCHAR(100) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),

    CONSTRAINT fk_draft_profile FOREIGN KEY (profile_id)
        REFERENCES profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Index for fast draft filtering by type for specific profile
CREATE INDEX IF NOT EXISTS idx_draft_profile_type
    ON draft_storage (profile_id, entity_type);

-- GIN index for effective full-text search in JSONB content
CREATE INDEX IF NOT EXISTS idx_draft_content_gin
    ON draft_storage USING GIN (content);