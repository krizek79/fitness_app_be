ALTER TABLE workout_exercise
ADD COLUMN workout_exercise_type varchar(255),
DROP COLUMN sets,
DROP COLUMN repetitions,
DROP COLUMN rest_duration;

CREATE TABLE workout_exercise_set (
    id BIGSERIAL PRIMARY KEY,
    workout_exercise_id BIGINT NOT NULL REFERENCES workout_exercise (id) ON DELETE CASCADE,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    workout_exercise_set_type VARCHAR(255),
    goal_repetitions INTEGER CHECK (goal_repetitions >= 1),
    actual_repetitions INTEGER CHECK (actual_repetitions >= 1),
    goal_weight DOUBLE PRECISION CHECK (goal_weight >= 0.125),
    actual_weight DOUBLE PRECISION CHECK (actual_weight >= 0.125),
    goal_time VARCHAR(255),
    actual_time VARCHAR(255),
    rest_duration VARCHAR(255),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    note VARCHAR(1024)
);

ALTER TABLE workout_exercise_set
ADD CONSTRAINT uk_workout_exercise_order_number UNIQUE (workout_exercise_id, order_number);