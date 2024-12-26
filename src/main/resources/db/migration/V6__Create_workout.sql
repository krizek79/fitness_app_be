CREATE TABLE workout (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author_id BIGINT,
    level VARCHAR(255),
    description VARCHAR(1000),
    CONSTRAINT fk_workout_author FOREIGN KEY (author_id) REFERENCES profile (id) ON DELETE SET NULL
);

CREATE TABLE workout_tag (
    workout_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    CONSTRAINT fk_workout_tag_workout FOREIGN KEY (workout_id) REFERENCES workout (id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_tag_tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE
);
