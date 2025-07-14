ALTER TABLE app_user
ADD CONSTRAINT fk_profile FOREIGN KEY(profile_id) REFERENCES profile(id);
