create table workout_session
(
    id                 bigserial primary key,
    workout_id         bigint                   not null references workout (id),
    week_workout_id    bigint references week_workout (id),
    status             varchar(255)             not null default 'NOT_STARTED',
    started_at         timestamp with time zone,
    finished_at        timestamp with time zone,
    created_at         timestamp with time zone not null default now(),
    created_by         varchar(255)             not null default 'SYSTEM',
    last_modified_at   timestamp with time zone not null default now(),
    last_modified_by   varchar(255)             not null default 'SYSTEM',
    version            bigint                   not null default 0
);
