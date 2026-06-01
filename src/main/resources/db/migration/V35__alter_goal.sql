ALTER TABLE goal
    ADD COLUMN profile_id BIGINT;

UPDATE goal g
SET profile_id = p.trainee_id
FROM plan p
WHERE g.plan_id = p.id;

ALTER TABLE goal
    ALTER COLUMN profile_id SET NOT NULL;

ALTER TABLE goal
    ADD CONSTRAINT fk_goal_profile
        FOREIGN KEY (profile_id)
            REFERENCES profile(id);

ALTER TABLE goal
    DROP CONSTRAINT IF EXISTS fk_goal_plan;

ALTER TABLE goal
    DROP COLUMN plan_id;