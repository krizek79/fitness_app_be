ALTER TABLE workout_exercise
    ALTER COLUMN workout_exercise_type SET NOT NULL;

ALTER TABLE workout_exercise
    RENAME COLUMN workout_exercise_type TO workout_exercise_metric;