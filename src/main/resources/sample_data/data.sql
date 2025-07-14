BEGIN;

-- === USERS & PROFILES ===
-- Insert users
INSERT INTO app_user (id, email, password, created_at, updated_at, active, locked, enabled, credentials_non_expired)
SELECT 1, 'coach@example.com', '', now(), now(), true, false, true, true
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 1);

INSERT INTO app_user (id, email, password, created_at, updated_at, active, locked, enabled, credentials_non_expired)
SELECT 2, 'client@example.com', '', now(), now(), true, false, true, true
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 2);

-- Insert profiles
INSERT INTO profile (id, user_id, name, preferred_weight_unit, deleted)
SELECT 1, 1, 'Coach Fox', 'KG', false
WHERE NOT EXISTS (SELECT 1 FROM profile WHERE id = 1);

INSERT INTO profile (id, user_id, name, preferred_weight_unit, deleted)
SELECT 2, 2, 'Client Doe', 'KG', false
WHERE NOT EXISTS (SELECT 1 FROM profile WHERE id = 2);

-- === COACH–CLIENT RELATION ===
INSERT INTO coach_client (id, coach_id, client_id, started_at, active)
SELECT 1, 1, 2, now(), true
WHERE NOT EXISTS (
    SELECT 1 FROM coach_client WHERE coach_id = 1 AND client_id = 2
);

-- === EXERCISES ===
INSERT INTO exercise (id, name)
SELECT * FROM (VALUES
    (1, 'Squat'),
    (2, 'Bench Press'),
    (3, 'Deadlift')
) AS tmp(id, name)
WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE id = tmp.id);

INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT * FROM (VALUES
    (1, 'LEGS'),
    (2, 'CHEST'),
    (3, 'BACK')
) AS tmp(exercise_id, muscle_group)
WHERE NOT EXISTS (
    SELECT 1 FROM exercise_muscle_group WHERE exercise_id = tmp.exercise_id AND muscle_group = tmp.muscle_group
);

-- === CYCLE, GOALS, TAGS ===
INSERT INTO cycle (id, author_id, trainee_id, name, description, level)
SELECT 1, 1, 2, 'Hypertrophy Cycle', '8-week hypertrophy block', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM cycle WHERE id = 1);

INSERT INTO goal (id, cycle_id, text, achieved)
SELECT * FROM (VALUES
    (1, 1, 'Add 10kg to squat', false),
    (2, 1, 'Improve endurance', false)
) AS tmp(id, cycle_id, text, achieved)
WHERE NOT EXISTS (SELECT 1 FROM goal WHERE id = tmp.id);

-- === WEEK ===
INSERT INTO week (id, cycle_id, order_number, completed, note)
SELECT * FROM (VALUES
    (1, 1, 1, false, 'Intro week'),
    (2, 1, 2, false, 'First real push')
) AS tmp(id, cycle_id, order_number, completed, note)
WHERE NOT EXISTS (SELECT 1 FROM week WHERE id = tmp.id);

-- === WORKOUTS, TAGS ===
INSERT INTO workout (id, name, author_id, trainee_id, description, is_template, weight_unit, note)
SELECT * FROM (VALUES
    (1, 'Leg Day A', 1, 2, 'Volume lower body day', false, 'KG', 'Tempo work'),
    (2, 'Push Day', 1, 2, 'Chest and triceps', false, 'KG', 'Focus on form')
) AS tmp(id, name, author_id, trainee_id, description, is_template, weight_unit, note)
WHERE NOT EXISTS (SELECT 1 FROM workout WHERE id = tmp.id);

INSERT INTO tag (id, name)
SELECT * FROM (VALUES
    (1, 'strength'),
    (2, 'volume')
) AS tmp(id, name)
WHERE NOT EXISTS (SELECT 1 FROM tag WHERE id = tmp.id);

INSERT INTO workout_tag (workout_id, tag_id)
SELECT * FROM (VALUES
    (1, 1),
    (1, 2)
) AS tmp(workout_id, tag_id)
WHERE NOT EXISTS (
    SELECT 1 FROM workout_tag WHERE workout_id = tmp.workout_id AND tag_id = tmp.tag_id
);

-- === WEEK_WORKOUT ===
INSERT INTO week_workout (id, workout_id, week_id, day_of_the_week, completed)
SELECT * FROM (VALUES
    (1, 1, 1, 1, false),
    (2, 2, 2, 3, false)
) AS tmp(id, workout_id, week_id, day_of_the_week, completed)
WHERE NOT EXISTS (SELECT 1 FROM week_workout WHERE id = tmp.id);

-- === WORKOUT_EXERCISES ===
INSERT INTO workout_exercise (id, order_number, workout_id, exercise_id, workout_exercise_type, note)
SELECT * FROM (VALUES
    (1, 1, 1, 1, 'WEIGHT', 'Controlled descent'),
    (2, 1, 2, 2, 'WEIGHT_TIME', 'Pause reps'),
    (3, 2, 2, 3, 'BODYWEIGHT', 'Speed focus')
) AS tmp(id, order_number, workout_id, exercise_id, workout_exercise_type, note)
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise WHERE id = tmp.id);

-- === WORKOUT_EXERCISE_SET ===
INSERT INTO workout_exercise_set (
    id, workout_exercise_id, order_number, workout_exercise_set_type,
    goal_repetitions, actual_repetitions, goal_weight, actual_weight,
    goal_time, actual_time, rest_duration, note
)
SELECT * FROM (VALUES
    (1, 1, 1, 'STRAIGHT_SET', 10, 10, 80, 80, 'PT4M30S', 'PT4M32S', 'PT3M30S', 'some note'),
    (2, 2, 1, 'TOP_SET', 5, 5, 90, 90, 'PT1M', 'PT1M1S', 'PT3M30S', 'some note'),
    (3, 2, 2, 'BACKOFF_SET', 8, 8, 75, 75, 'PT1M', 'PT1M', 'PT3M30S', 'some note'),
    (4, 3, 1, 'WARMUP', 10, 10, 40, 40, 'PT30S', 'PT30S', 'PT3M30S', 'some note')
) AS tmp(
    id, workout_exercise_id, order_number, workout_exercise_set_type,
    goal_repetitions, actual_repetitions, goal_weight, actual_weight,
    goal_time, actual_time, rest_duration, note
)
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise_set WHERE id = tmp.id);

COMMIT;
