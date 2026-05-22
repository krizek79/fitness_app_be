BEGIN;

-- =========================================================
-- TABLE + SEQUENCE
-- =========================================================

ALTER TABLE cycle RENAME TO plan;

ALTER SEQUENCE IF EXISTS cycle_id_seq
    RENAME TO plan_id_seq;

-- =========================================================
-- PRIMARY KEY CONSTRAINT
-- =========================================================

ALTER TABLE plan
    RENAME CONSTRAINT cycle_pkey TO plan_pkey;

-- =========================================================
-- REMOVE COLUMN
-- =========================================================

ALTER TABLE plan
    DROP COLUMN IF EXISTS level;

-- =========================================================
-- WEEK TABLE
-- =========================================================

-- rename FK column
ALTER TABLE week
    RENAME COLUMN cycle_id TO plan_id;

-- rename FK constraint
ALTER TABLE week
    RENAME CONSTRAINT fk_cycle TO fk_week_plan;

-- =========================================================
-- GOAL TABLE
-- =========================================================

-- rename FK column
ALTER TABLE goal
    RENAME COLUMN cycle_id TO plan_id;

-- rename FK constraint
ALTER TABLE goal
    RENAME CONSTRAINT fk_cycle TO fk_goal_plan;

COMMIT;