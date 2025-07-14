CREATE TABLE profile (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(64),
    profile_picture_url TEXT,
    bio VARCHAR(128),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    preferred_weight_unit VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(id) ON DELETE CASCADE
);
