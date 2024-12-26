ALTER TABLE app_user
ADD COLUMN profile_id BIGINT;

ALTER TABLE app_user
ADD CONSTRAINT fk_user_profile_id FOREIGN KEY (profile_id) REFERENCES profile (id) ON DELETE SET NULL;
