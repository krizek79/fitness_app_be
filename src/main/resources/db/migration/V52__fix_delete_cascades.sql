-- When a workout is deleted, cascade to its sessions (training history is gone with the workout)
alter table workout_session
    drop constraint workout_session_workout_id_fkey,
    add constraint workout_session_workout_id_fkey
        foreign key (workout_id) references workout (id) on delete cascade;

-- When a week_workout is deleted, preserve the session but clear the reference
alter table workout_session
    drop constraint workout_session_week_workout_id_fkey,
    add constraint workout_session_week_workout_id_fkey
        foreign key (week_workout_id) references week_workout (id) on delete set null;

-- When a workout_session is DB-cascade-deleted (via workout), cascade to its exercise sessions
alter table workout_exercise_session
    drop constraint workout_exercise_session_workout_session_id_fkey,
    add constraint workout_exercise_session_workout_session_id_fkey
        foreign key (workout_session_id) references workout_session (id) on delete cascade;

-- When a workout_exercise is deleted (orphanRemoval), cascade to its exercise sessions
alter table workout_exercise_session
    drop constraint workout_exercise_session_workout_exercise_id_fkey,
    add constraint workout_exercise_session_workout_exercise_id_fkey
        foreign key (workout_exercise_id) references workout_exercise (id) on delete cascade;

-- When a workout_exercise_session is DB-cascade-deleted, cascade to its set results
alter table workout_exercise_set_result
    drop constraint workout_exercise_set_result_workout_exercise_session_id_fkey,
    add constraint workout_exercise_set_result_workout_exercise_session_id_fkey
        foreign key (workout_exercise_session_id) references workout_exercise_session (id) on delete cascade;
