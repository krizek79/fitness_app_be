CREATE TABLE exercise (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE exercise_muscle_group (
    exercise_id BIGINT NOT NULL REFERENCES exercise (id) ON DELETE CASCADE,
    muscle_group VARCHAR(255) NOT NULL
);
