BEGIN;

-- 1. Insert exercises
INSERT INTO exercise (name) SELECT 'Squat' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Squat');
INSERT INTO exercise (name) SELECT 'Bench Press' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Bench Press');
INSERT INTO exercise (name) SELECT 'Deadlift' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Deadlift');
INSERT INTO exercise (name) SELECT 'Overhead Press' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Overhead Press');
INSERT INTO exercise (name) SELECT 'Pull-Up' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Pull-Up');
INSERT INTO exercise (name) SELECT 'Lunge' WHERE NOT EXISTS (SELECT 1 FROM exercise WHERE name='Lunge');

-- 2. Insert muscle groups
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'LEGS' FROM exercise e WHERE e.name='Squat'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='LEGS');
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'CHEST' FROM exercise e WHERE e.name='Bench Press'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='CHEST');
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'BACK' FROM exercise e WHERE e.name='Deadlift'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='BACK');
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'SHOULDERS' FROM exercise e WHERE e.name='Overhead Press'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='SHOULDERS');
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'BACK' FROM exercise e WHERE e.name='Pull-Up'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='BACK');
INSERT INTO exercise_muscle_group (exercise_id, muscle_group)
SELECT e.id, 'LEGS' FROM exercise e WHERE e.name='Lunge'
  AND NOT EXISTS (SELECT 1 FROM exercise_muscle_group WHERE exercise_id=e.id AND muscle_group='LEGS');

-- 3. Insert tags
INSERT INTO tag (name) SELECT 'strength' WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name='strength');
INSERT INTO tag (name) SELECT 'hypertrophy' WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name='hypertrophy');
INSERT INTO tag (name) SELECT 'mobility' WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name='mobility');
INSERT INTO tag (name) SELECT 'endurance' WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name='endurance');
INSERT INTO tag (name) SELECT 'powerlifting' WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name='powerlifting');

-- 4. Insert workouts
INSERT INTO workout (name, profile_id, description, is_template, weight_unit, note, author_id, trainee_id)
SELECT 'Full Body Strength', p1.id, 'Heavy compound lifts', false, 'KG', 'Use RPE 8-9', p1.id, p2.id
FROM profile p1 JOIN profile p2 ON p2.name='Jane Smith'
WHERE p1.name='John Doe'
  AND NOT EXISTS (SELECT 1 FROM workout WHERE name='Full Body Strength');

INSERT INTO workout (name, profile_id, description, is_template, weight_unit, note, author_id, trainee_id)
SELECT 'Upper Body Hypertrophy', p.id, 'Focus on volume for upper body', true, 'LB', '', p.id, NULL
FROM profile p WHERE p.name='Jane Smith'
  AND NOT EXISTS (SELECT 1 FROM workout WHERE name='Upper Body Hypertrophy');

INSERT INTO workout (name, profile_id, description, is_template, weight_unit, note, author_id, trainee_id)
SELECT 'Push Day', p.id, 'Shoulders, Chest, Triceps focus', false, 'KG', 'Include OHP variations', p.id, p.id
FROM profile p WHERE p.name='Jane Smith'
  AND NOT EXISTS (SELECT 1 FROM workout WHERE name='Push Day');

-- 5. Insert workout exercises
INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 1, 4, 5, 'PT2M', 'Pause squats', 'WEIGHT'
FROM workout w JOIN exercise e ON e.name='Squat'
WHERE w.name='Full Body Strength'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND order_number=1);

INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 2, 3, 8, 'PT90S', 'Bodyweight focused', 'BODYWEIGHT'
FROM workout w JOIN exercise e ON e.name='Pull-Up'
WHERE w.name='Upper Body Hypertrophy'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND order_number=2);

INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 1, 3, 6, 'PT90S', 'Standing strict press', 'WEIGHT'
FROM workout w JOIN exercise e ON e.name='Overhead Press'
WHERE w.name='Push Day'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND order_number=1);

-- Additional workout_exercises
INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 1, 4, 10, 'PT90S', 'Focus on control', 'WEIGHT'
FROM workout w JOIN exercise e ON e.name='Bench Press'
WHERE w.name='Upper Body Hypertrophy'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND exercise_id=e.id);

INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 2, 3, 8, 'PT2M', 'Walking lunges with dumbbells', 'WEIGHT'
FROM workout w JOIN exercise e ON e.name='Lunge'
WHERE w.name='Full Body Strength'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND exercise_id=e.id);

INSERT INTO workout_exercise (workout_id, exercise_id, order_number, sets, repetitions, rest_duration, note, workout_exercise_type)
SELECT w.id, e.id, 3, 3, 5, 'PT3M', 'Conventional deadlifts', 'WEIGHT'
FROM workout w JOIN exercise e ON e.name='Deadlift'
WHERE w.name='Full Body Strength'
  AND NOT EXISTS (SELECT 1 FROM workout_exercise WHERE workout_id=w.id AND exercise_id=e.id);

-- 6. Insert workout exercise sets
WITH wes1 AS (
  SELECT we.id FROM workout_exercise we
  JOIN workout w ON we.workout_id=w.id
  WHERE w.name='Push Day' AND we.order_number=1
)
INSERT INTO workout_exercise_set (
  actual_repetitions, actual_time, actual_weight, completed,
  goal_repetitions, goal_time, goal_weight, note,
  order_number, rest_duration, workout_exercise_set_type, workout_exercise_id
)
SELECT 6, NULL, 40.0, true, 6, NULL, 40.0, 'Solid overhead lockout', 1, 'PT90S', 'STRAIGHT_SET', wes1.id
FROM wes1
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise_set WHERE workout_exercise_id = wes1.id AND order_number = 1);

-- Add Bench Press set
WITH wes2 AS (
  SELECT we.id FROM workout_exercise we
  JOIN workout w ON we.workout_id = w.id
  JOIN exercise e ON we.exercise_id = e.id
  WHERE w.name = 'Upper Body Hypertrophy' AND e.name = 'Bench Press'
)
INSERT INTO workout_exercise_set (
  actual_repetitions, actual_time, actual_weight, completed,
  goal_repetitions, goal_time, goal_weight, note,
  order_number, rest_duration, workout_exercise_set_type, workout_exercise_id
)
SELECT 10, NULL, 60.0, true, 10, NULL, 60.0, 'Controlled tempo', 1, 'PT90S', 'STRAIGHT_SET', wes2.id
FROM wes2
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise_set WHERE workout_exercise_id = wes2.id AND order_number = 1);

-- Add Lunge set
WITH wes3 AS (
  SELECT we.id FROM workout_exercise we
  JOIN workout w ON we.workout_id = w.id
  JOIN exercise e ON we.exercise_id = e.id
  WHERE w.name = 'Full Body Strength' AND e.name = 'Lunge'
)
INSERT INTO workout_exercise_set (
  actual_repetitions, actual_time, actual_weight, completed,
  goal_repetitions, goal_time, goal_weight, note,
  order_number, rest_duration, workout_exercise_set_type, workout_exercise_id
)
SELECT 8, NULL, 20.0, true, 8, NULL, 20.0, 'Use dumbbells', 1, 'PT2M', 'STRAIGHT_SET', wes3.id
FROM wes3
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise_set WHERE workout_exercise_id = wes3.id AND order_number = 1);

-- Add Deadlift set
WITH wes4 AS (
  SELECT we.id FROM workout_exercise we
  JOIN workout w ON we.workout_id = w.id
  JOIN exercise e ON we.exercise_id = e.id
  WHERE w.name = 'Full Body Strength' AND e.name = 'Deadlift'
)
INSERT INTO workout_exercise_set (
  actual_repetitions, actual_time, actual_weight, completed,
  goal_repetitions, goal_time, goal_weight, note,
  order_number, rest_duration, workout_exercise_set_type, workout_exercise_id
)
SELECT 5, NULL, 120.0, true, 5, NULL, 120.0, 'Flat back, slow down', 1, 'PT3M', 'STRAIGHT_SET', wes4.id
FROM wes4
WHERE NOT EXISTS (SELECT 1 FROM workout_exercise_set WHERE workout_exercise_id = wes4.id AND order_number = 1);

-- 7. Insert cycle
INSERT INTO cycle (author_id, trainee_id, name, description, level)
SELECT a.id, b.id, 'Strength Block 1', '4-week strength focused cycle', 'INTERMEDIATE'
FROM profile a JOIN profile b ON b.name='Jane Smith'
WHERE a.name='John Doe'
  AND NOT EXISTS (SELECT 1 FROM cycle WHERE name='Strength Block 1');

-- 8. Insert goals
INSERT INTO goal (cycle_id, achieved, text)
SELECT c.id, false, 'Increase squat max by 10kg'
FROM cycle c WHERE c.name='Strength Block 1'
  AND NOT EXISTS (SELECT 1 FROM goal WHERE cycle_id=c.id AND text='Increase squat max by 10kg');

-- 9. Insert weeks
INSERT INTO week (cycle_id, order_number, completed, note)
SELECT c.id, 1, false, 'Start with lower volume'
FROM cycle c WHERE c.name='Strength Block 1'
  AND NOT EXISTS (SELECT 1 FROM week WHERE cycle_id=c.id AND order_number=1);

-- 10. Insert week_workouts
INSERT INTO week_workout (day_of_the_week, completed, week_id, workout_id)
SELECT 1, false, w.id, wo.id
FROM week w JOIN workout wo ON wo.name='Full Body Strength'
WHERE w.cycle_id=(SELECT id FROM cycle WHERE name='Strength Block 1') AND w.order_number=1
  AND NOT EXISTS (SELECT 1 FROM week_workout ww WHERE ww.week_id=w.id AND ww.workout_id=wo.id);

INSERT INTO week_workout (day_of_the_week, completed, week_id, workout_id)
SELECT 3, false, w.id, wo.id
FROM week w JOIN workout wo ON wo.name='Push Day'
WHERE w.cycle_id=(SELECT id FROM cycle WHERE name='Strength Block 1') AND w.order_number=1
  AND NOT EXISTS (SELECT 1 FROM week_workout ww WHERE ww.week_id=w.id AND ww.workout_id=wo.id);

-- 11. Insert workout_tag relationships
INSERT INTO workout_tag (workout_id, tag_id)
SELECT w.id, t.id FROM workout w JOIN tag t ON t.name='strength'
WHERE w.name IN ('Full Body Strength', 'Push Day')
  AND NOT EXISTS (SELECT 1 FROM workout_tag wt WHERE wt.workout_id=w.id AND wt.tag_id=t.id);

INSERT INTO workout_tag (workout_id, tag_id)
SELECT w.id, t.id FROM workout w JOIN tag t ON t.name='hypertrophy'
WHERE w.name IN ('Upper Body Hypertrophy')
  AND NOT EXISTS (SELECT 1 FROM workout_tag wt WHERE wt.workout_id=w.id AND wt.tag_id=t.id);

INSERT INTO workout_tag (workout_id, tag_id)
SELECT w.id, t.id FROM workout w JOIN tag t ON t.name='powerlifting'
WHERE w.name IN ('Full Body Strength')
  AND NOT EXISTS (SELECT 1 FROM workout_tag wt WHERE wt.workout_id=w.id AND wt.tag_id=t.id);

COMMIT;
