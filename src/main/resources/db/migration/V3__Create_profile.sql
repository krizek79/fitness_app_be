CREATE TABLE profile (
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    name VARCHAR(64),
    profile_picture_url VARCHAR(255),
    bio VARCHAR(128),
    CONSTRAINT fk_user_profile FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
