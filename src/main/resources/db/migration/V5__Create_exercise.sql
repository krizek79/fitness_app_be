CREATE TABLE exercise (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE exercise_muscle_group (
    exercise_id BIGINT NOT NULL,
    muscle_group VARCHAR(255) NOT NULL,
    CONSTRAINT fk_exercise_muscle_group FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE
);
