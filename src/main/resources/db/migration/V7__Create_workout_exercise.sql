CREATE TABLE workout_exercise (
    id SERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL REFERENCES workout (id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL REFERENCES exercise (id) ON DELETE CASCADE,
    sets INTEGER,
    repetitions INTEGER,
    rest_duration VARCHAR(255)
);
