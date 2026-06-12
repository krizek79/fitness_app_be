CREATE TABLE exercise_muscle_role
(
    id          BIGSERIAL PRIMARY KEY,
    exercise_id BIGINT      NOT NULL,
    muscle      VARCHAR(50) NOT NULL,
    type        VARCHAR(50) NOT NULL,

    CONSTRAINT fk_exercise_muscle_role_exercise
        FOREIGN KEY (exercise_id)
            REFERENCES exercise (id)
            ON DELETE CASCADE
);