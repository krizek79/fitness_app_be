CREATE TABLE workout_exercise_set (
    id BIGSERIAL PRIMARY KEY,
    workout_exercise_id BIGINT NOT NULL,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    workout_exercise_set_type VARCHAR(255),
    goal_repetitions INTEGER CHECK (goal_repetitions >= 1),
    actual_repetitions INTEGER CHECK (actual_repetitions >= 1),
    goal_weight NUMERIC CHECK (goal_weight >= 0.125),
    actual_weight NUMERIC CHECK (actual_weight >= 0.125),
    goal_time VARCHAR(255),
    actual_time VARCHAR(255),
    rest_duration VARCHAR(255),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    note VARCHAR(1024),
    CONSTRAINT fk_workout_exercise FOREIGN KEY(workout_exercise_id) REFERENCES workout_exercise(id) ON DELETE CASCADE
);
