create table battle
(
    id                      serial not null
        constraint battle_pkey
            primary key,
    attack_subdivision_id   integer,
    defender_subdivision_id integer
);

create table participating_subdivision
(
    id               serial not null
        constraint participating_subdivision_pkey
            primary key,
    subdivision_id   integer,
    subdivision_type varchar(255)
);

create table shot
(
    id          integer not null
        constraint shot_pkey
            primary key,
    damage      double precision,
    shot_result varchar(255),
    target_id   integer,
    target_type varchar(255),
    battle_id   integer
        constraint fk94t3b7xyfdmptxbhe19vh96fx
            references battle
);

create table unit_data
(
    id               serial not null
        constraint unit_data_pkey
            primary key,
    unit_id          integer,
    unit_type        varchar(255),
    is_alive         boolean,
    posx             integer,
    posy             integer,
    protection_level integer,
    taken_damage     double precision,
    battle_id        integer
        constraint fkpq37a5r1hnnb6ry35rjmpdobw
            references battle
);

