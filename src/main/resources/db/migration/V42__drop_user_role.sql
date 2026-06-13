DROP TABLE user_role;

CREATE UNIQUE INDEX idx_app_user_keycloak_id ON app_user (keycloak_id);