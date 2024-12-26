CREATE TABLE workout_exercise (
    id SERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    sets INTEGER,
    repetitions INTEGER,
    rest_duration VARCHAR(255),
    CONSTRAINT fk_workout_exercise_workout FOREIGN KEY (workout_id) REFERENCES workout (id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_exercise_exercise FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE
);
