CREATE TABLE workout_exercise (
    id BIGSERIAL PRIMARY KEY,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    workout_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    workout_exercise_type VARCHAR(255),
    note VARCHAR(1024),
    CONSTRAINT fk_workout FOREIGN KEY(workout_id) REFERENCES workout(id) ON DELETE CASCADE,
    CONSTRAINT fk_exercise FOREIGN KEY(exercise_id) REFERENCES exercise(id) ON DELETE CASCADE
);
