CREATE TABLE equipment
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255)             NOT NULL,
    thumbnail_url    VARCHAR(255),
    deleted          BOOLEAN                  NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by       VARCHAR(255)             NOT NULL,
    last_modified_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_by VARCHAR(255)             NOT NULL,
    version          BIGINT                   NOT NULL DEFAULT 0,
    deleted_at       TIMESTAMP WITH TIME ZONE,
    deleted_by       VARCHAR(255)
);