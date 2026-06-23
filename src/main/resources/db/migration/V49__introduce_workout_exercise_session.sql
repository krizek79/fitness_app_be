create table workout_exercise_session
(
    id                   bigserial primary key,
    workout_session_id   bigint                   not null references workout_session (id),
    workout_exercise_id  bigint                   not null references workout_exercise (id),
    order_number         integer                  not null,
    note                 varchar(1024),
    created_at           timestamp with time zone not null default now(),
    created_by           varchar(255)             not null default 'SYSTEM',
    last_modified_at     timestamp with time zone not null default now(),
    last_modified_by     varchar(255)             not null default 'SYSTEM',
    version              bigint                   not null default 0
);
