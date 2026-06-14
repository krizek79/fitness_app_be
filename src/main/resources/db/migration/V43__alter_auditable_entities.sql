-- app_user
alter table app_user
drop column deleted_at;

alter table app_user
drop column deleted_by;

-- profile
alter table profile
    drop column deleted_at;

alter table profile
    drop column deleted_by;

-- coaching_contract
alter table coaching_contract
    drop column deleted_at;

alter table coaching_contract
    drop column deleted_by;

-- plan
alter table plan
    drop column deleted_at;

alter table plan
    drop column deleted_by;

-- exercise
alter table exercise
    drop column deleted_at;

alter table exercise
    drop column deleted_by;

-- goal
alter table goal
    drop column deleted_at;

alter table goal
    drop column deleted_by;

-- tag
alter table tag
    drop column deleted_at;

alter table tag
    drop column deleted_by;

-- week
alter table week
    drop column deleted_at;

alter table week
    drop column deleted_by;

-- workout
alter table workout
    drop column deleted_at;

alter table workout
    drop column deleted_by;

-- week_workout
alter table week_workout
    drop column deleted_at;

alter table week_workout
    drop column deleted_by;

-- workout_exercise
alter table workout_exercise
    drop column deleted_at;

alter table workout_exercise
    drop column deleted_by;

-- workout_exercise_set
alter table workout_exercise_set
    drop column deleted_at;

alter table workout_exercise_set
    drop column deleted_by;

-- draft
alter table draft_storage
    drop column deleted_at;

alter table draft_storage
    drop column deleted_by;