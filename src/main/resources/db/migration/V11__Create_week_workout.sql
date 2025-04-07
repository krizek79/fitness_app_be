CREATE TABLE week_workout (
    id SERIAL PRIMARY KEY,
    day_of_the_week INTEGER NOT NULL CHECK (day_of_the_week >= 0 AND day_of_the_week <= 6),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    week_id BIGINT NOT NULL REFERENCES week (id) ON DELETE CASCADE,
    workout_id BIGINT NOT NULL REFERENCES workout (id) ON DELETE CASCADE,
    CONSTRAINT unique_workout_id UNIQUE (workout_id)
);