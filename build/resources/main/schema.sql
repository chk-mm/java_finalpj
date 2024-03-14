create table if not exists users
(
    id bigint auto_increment primary key,
    name text not null
);

create table if not exists activity
(
    id bigint auto_increment primary key,
    user_id bigserial references users(id) on delete cascade,
    name text not null,
    kcal_per_minute decimal not null,
    unique (user_id, name)
);

create table if not exists exercise
(
    id bigint auto_increment primary key,
    user_id bigint not null references users(id) on delete cascade,
    activity_id bigint not null references activity(id),
    start_time timestamp not null,
    duration bigint not null
);
