ALTER TABLE workout_exercise_set
    RENAME COLUMN goal_time TO goal_time_seconds;

ALTER TABLE workout_exercise_set
    RENAME COLUMN actual_time TO actual_time_seconds;

ALTER TABLE workout_exercise_set
    RENAME COLUMN rest_duration TO rest_duration_seconds;


ALTER TABLE workout_exercise_set
    ALTER COLUMN goal_time_seconds TYPE BIGINT
        USING EXTRACT(EPOCH FROM goal_time_seconds::interval);

ALTER TABLE workout_exercise_set
    ALTER COLUMN actual_time_seconds TYPE BIGINT
        USING EXTRACT(EPOCH FROM actual_time_seconds::interval);

ALTER TABLE workout_exercise_set
    ALTER COLUMN rest_duration_seconds TYPE BIGINT
        USING EXTRACT(EPOCH FROM rest_duration_seconds::interval);

ALTER TABLE workout_exercise_set
    ADD CONSTRAINT chk_goal_time_seconds_non_negative
        CHECK (goal_time_seconds >= 0);

ALTER TABLE workout_exercise_set
    ADD CONSTRAINT chk_actual_time_seconds_non_negative
        CHECK (actual_time_seconds >= 0);

ALTER TABLE workout_exercise_set
    ADD CONSTRAINT chk_rest_duration_seconds_non_negative
        CHECK (rest_duration_seconds >= 0);