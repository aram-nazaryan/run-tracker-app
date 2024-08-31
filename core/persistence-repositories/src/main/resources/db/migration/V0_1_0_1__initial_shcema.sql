do $$ begin
    create type gender as enum ('MALE', 'FEMALE');
exception
    when duplicate_object then null;
end $$;

create table if not exists _user (
      id uuid                                 not null,
      first_name varchar(100)                 not null,
      last_name varchar(100)                  not null,
      birth_date date                         not null,
      gender gender                           not null,
      created timestamp                       not null,
      updated timestamp                       not null,
      deleted timestamp,
      constraint pk_user primary key (id)
);

create table if not exists run (
       id uuid                                    not null,
       user_id uuid                               not null,
       start_position geometry(POINT,4326)        not null,
       start_time timestamp                       not null,
       finish_position geometry(POINT,4326),
       finish_time timestamp,
       distance integer,
       speed double precision,
       created timestamp                          not null,
       updated timestamp                          not null,
       deleted timestamp,
       constraint pk_run primary key (id),
       constraint fk_run_user foreign key (user_id) references _user (id)
);