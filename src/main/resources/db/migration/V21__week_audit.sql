ALTER TABLE week
    ADD COLUMN created_at TIMESTAMPTZ,
    ADD COLUMN created_by VARCHAR(100),
    ADD COLUMN last_modified_at TIMESTAMPTZ,
    ADD COLUMN last_modified_by VARCHAR(100),
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN deleted_at TIMESTAMPTZ,
    ADD COLUMN deleted_by VARCHAR(100);

UPDATE week
SET
    created_at = NOW(),
    created_by = 'SYSTEM',
    last_modified_at = NOW(),
    last_modified_by = 'SYSTEM'
WHERE created_at IS NULL;

ALTER TABLE week
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN created_by SET NOT NULL,
    ALTER COLUMN last_modified_at SET NOT NULL,
    ALTER COLUMN last_modified_by SET NOT NULL;
