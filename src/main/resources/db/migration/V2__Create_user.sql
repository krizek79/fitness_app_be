CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    active BOOLEAN,
    locked BOOLEAN,
    enabled BOOLEAN,
    credentials_non_expired BOOLEAN
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    role VARCHAR(255) NOT NULL
);
