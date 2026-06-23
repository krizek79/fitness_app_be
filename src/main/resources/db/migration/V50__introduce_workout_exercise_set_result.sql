create table workout_exercise_set_result
(
    id                           bigserial primary key,
    workout_exercise_session_id  bigint                   not null references workout_exercise_session (id),
    workout_exercise_set_id      bigint references workout_exercise_set (id),
    order_number                 integer                  not null,
    workout_exercise_set_type    varchar(255),
    repetitions                  integer,
    weight                       numeric(10, 3),
    time_seconds                 bigint,
    distance_meters              numeric,
    rest_duration_seconds        bigint,
    completed                    boolean                  not null default false,
    note                         varchar(1024),
    created_at                   timestamp with time zone not null default now(),
    created_by                   varchar(255)             not null default 'SYSTEM',
    last_modified_at             timestamp with time zone not null default now(),
    last_modified_by             varchar(255)             not null default 'SYSTEM',
    version                      bigint                   not null default 0
);
