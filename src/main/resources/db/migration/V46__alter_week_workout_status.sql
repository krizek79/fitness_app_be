alter table week_workout
    add column status VARCHAR(255) NOT NULL default 'NOT_STARTED';

alter table week_workout
    drop column completed;
