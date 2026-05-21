-- Add order_in_the_day column to week_workout
ALTER TABLE week_workout ADD COLUMN order_in_the_day INTEGER NOT NULL DEFAULT 1;

-- Update day_of_week to use enum-like values instead of integers (1-7 to MONDAY-SUNDAY)
-- Create a temporary column to store the new values
ALTER TABLE week_workout ADD COLUMN day_of_week_temp VARCHAR(20);

-- Map integer values to day names
UPDATE week_workout SET day_of_week_temp =
  CASE day_of_the_week
    WHEN 1 THEN 'MONDAY'
    WHEN 2 THEN 'TUESDAY'
    WHEN 3 THEN 'WEDNESDAY'
    WHEN 4 THEN 'THURSDAY'
    WHEN 5 THEN 'FRIDAY'
    WHEN 6 THEN 'SATURDAY'
    WHEN 7 THEN 'SUNDAY'
  END;

-- Drop the old column
ALTER TABLE week_workout DROP COLUMN day_of_the_week;

-- Rename the temp column to day_of_week
ALTER TABLE week_workout RENAME COLUMN day_of_week_temp TO day_of_week;

-- Add constraint for valid day of week values
ALTER TABLE week_workout ADD CONSTRAINT check_day_of_week
  CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'));

-- Add constraint for valid order values
ALTER TABLE week_workout ADD CONSTRAINT check_order_in_the_day
  CHECK (order_in_the_day >= 1);

