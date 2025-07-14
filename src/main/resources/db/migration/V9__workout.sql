CREATE TABLE workout (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    author_id BIGINT NOT NULL,
    trainee_id BIGINT,
    description VARCHAR(1000),
    is_template BOOLEAN NOT NULL DEFAULT FALSE,
    weight_unit VARCHAR(255) NOT NULL,
    note VARCHAR(1024),
    CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT fk_trainee FOREIGN KEY(trainee_id) REFERENCES profile(id) ON DELETE SET NULL
);

CREATE TABLE workout_tag (
    workout_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY(workout_id, tag_id),
    CONSTRAINT fk_workout FOREIGN KEY(workout_id) REFERENCES workout(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY(tag_id) REFERENCES tag(id) ON DELETE CASCADE
);
