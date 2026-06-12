ALTER TABLE workout_exercise_set
    ADD COLUMN goal_distance_meters NUMERIC(10, 3);

ALTER TABLE workout_exercise_set
    ADD COLUMN actual_distance_meters NUMERIC(10, 3);