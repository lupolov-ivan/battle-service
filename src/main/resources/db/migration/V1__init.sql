create table battle
(
    battle_id                      serial not null
        constraint battle_pkey
            primary key,
    attack_subdivision_id   integer,
    defender_subdivision_id integer
);

create table shot
(
    id          serial not null
        constraint shot_pkey
            primary key,
    damage      double precision,
    shot_result varchar(255),
    target_id   integer,
    target_type varchar(255),
    battle_id   integer
        constraint fk_shot_battle_id_1
            references battle (battle_id)
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
        constraint fk_unit_data_battle_id_1
            references battle (battle_id)
);

