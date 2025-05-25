CREATE TABLE workout_exercise (
    id SERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL REFERENCES workout (id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL REFERENCES exercise (id) ON DELETE CASCADE,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    sets INTEGER,
    repetitions INTEGER,
    rest_duration VARCHAR(255)
);

ALTER TABLE workout_exercise ADD CONSTRAINT uk_workout_order_number UNIQUE (workout_id, order_number);
