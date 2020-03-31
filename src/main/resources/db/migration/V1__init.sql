CREATE TABLE unit_data (
    id SERIAL NOT NULL PRIMARY KEY,
    posx INTEGER,
    posy INTEGER,
    protection_level INTEGER,
    taken_damage DOUBLE PRECISION
);

CREATE UNIQUE INDEX unit_data_pos_x_pos_y
	ON unit_data (posx, posy);