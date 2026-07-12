BEGIN;

ALTER TABLE coaching_contract
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

UPDATE coaching_contract
    SET status = CASE WHEN active THEN 'ACTIVE' ELSE 'TERMINATED' END;

ALTER TABLE coaching_contract
    ALTER COLUMN status DROP DEFAULT;

ALTER TABLE coaching_contract
    DROP COLUMN active;

ALTER TABLE coaching_contract
    ADD COLUMN ends_at TIMESTAMPTZ NULL;

ALTER TABLE coaching_contract
    DROP CONSTRAINT uq_coach_client;

COMMIT;
