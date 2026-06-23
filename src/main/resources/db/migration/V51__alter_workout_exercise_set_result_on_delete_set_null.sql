alter table workout_exercise_set_result
    drop constraint workout_exercise_set_result_workout_exercise_set_id_fkey,
    add constraint workout_exercise_set_result_workout_exercise_set_id_fkey
        foreign key (workout_exercise_set_id) references workout_exercise_set (id) on delete set null;
