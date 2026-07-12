BEGIN;

ALTER TABLE coaching_contract
    DROP COLUMN started_at,
    DROP COLUMN ends_at;

COMMIT;
