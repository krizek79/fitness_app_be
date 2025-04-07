CREATE TABLE cycle (
    id SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES profile (id) ON DELETE CASCADE,
    trainee_id BIGINT REFERENCES profile (id),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000)
);