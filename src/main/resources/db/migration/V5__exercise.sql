CREATE TABLE exercise (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE exercise_muscle_group (
    exercise_id BIGINT NOT NULL,
    muscle_group VARCHAR(255) NOT NULL,
    PRIMARY KEY(exercise_id, muscle_group),
    CONSTRAINT fk_exercise FOREIGN KEY(exercise_id) REFERENCES exercise(id) ON DELETE CASCADE
);
