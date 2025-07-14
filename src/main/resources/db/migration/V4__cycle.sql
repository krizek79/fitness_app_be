CREATE TABLE cycle (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    trainee_id BIGINT,
    name TEXT NOT NULL,
    description VARCHAR(2000),
    level VARCHAR(255),
    CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT fk_trainee FOREIGN KEY(trainee_id) REFERENCES profile(id) ON DELETE SET NULL
);
