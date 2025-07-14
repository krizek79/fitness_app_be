CREATE TABLE week_workout (
    id BIGSERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL UNIQUE,
    week_id BIGINT NOT NULL,
    day_of_the_week INTEGER NOT NULL CHECK (day_of_the_week BETWEEN 1 AND 7),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_workout FOREIGN KEY(workout_id) REFERENCES workout(id) ON DELETE CASCADE,
    CONSTRAINT fk_week FOREIGN KEY(week_id) REFERENCES week(id) ON DELETE CASCADE
);
