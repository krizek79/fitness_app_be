-- Exercise table

ALTER TABLE exercise
    ADD COLUMN thumbnail_url VARCHAR(255);

ALTER TABLE exercise
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE exercise
    ADD COLUMN exercise_category VARCHAR(255) NOT NULL DEFAULT 'CHANGE_ME';

UPDATE exercise
SET name = 'CHANGE_ME'
WHERE name IS NULL;

ALTER TABLE exercise
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE exercise
    RENAME COLUMN name TO title;

-- Movement patterns

CREATE TABLE exercise_movement_pattern
(
    exercise_id      BIGINT       NOT NULL,
    movement_pattern VARCHAR(255) NOT NULL,

    CONSTRAINT fk_exercise_movement_pattern_exercise
        FOREIGN KEY (exercise_id)
            REFERENCES exercise (id)
            ON DELETE CASCADE
);

-- Required equipment

CREATE TABLE exercise_required_equipment
(
    exercise_id  BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,

    CONSTRAINT pk_exercise_required_equipment
        PRIMARY KEY (exercise_id, equipment_id),

    CONSTRAINT fk_exercise_required_equipment_exercise
        FOREIGN KEY (exercise_id)
            REFERENCES exercise (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_exercise_required_equipment_equipment
        FOREIGN KEY (equipment_id)
            REFERENCES equipment (id)
);