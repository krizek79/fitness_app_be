BEGIN;

ALTER TABLE profile ADD COLUMN public_id VARCHAR(9);

UPDATE profile
SET public_id = upper(substr(md5(random()::text || clock_timestamp()::text || id::text), 1, 4)
    || '-' || substr(md5(random()::text || clock_timestamp()::text || id::text), 5, 4));

ALTER TABLE profile ALTER COLUMN public_id SET NOT NULL;
ALTER TABLE profile ADD CONSTRAINT uq_profile_public_id UNIQUE (public_id);

COMMIT;
