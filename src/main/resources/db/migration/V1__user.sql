CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    password TEXT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    profile_id BIGINT UNIQUE,
    active BOOLEAN,
    locked BOOLEAN,
    enabled BOOLEAN,
    credentials_non_expired BOOLEAN
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY(user_id, role),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(id) ON DELETE CASCADE
);
