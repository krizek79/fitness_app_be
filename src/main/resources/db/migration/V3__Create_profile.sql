CREATE TABLE profile (
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    name VARCHAR(64),
    profile_picture_url VARCHAR(255),
    bio VARCHAR(128),
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);
