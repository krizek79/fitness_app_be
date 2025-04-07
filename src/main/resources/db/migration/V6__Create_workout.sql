CREATE TABLE workout (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author_id BIGINT REFERENCES profile (id),
    level VARCHAR(255),
    description VARCHAR(1000),
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE workout_tag (
    workout_id BIGINT NOT NULL REFERENCES workout (id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tag (id) ON DELETE CASCADE
);
