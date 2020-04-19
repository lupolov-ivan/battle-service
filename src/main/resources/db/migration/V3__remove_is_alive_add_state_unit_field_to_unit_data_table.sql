ALTER TABLE unit_data
    DROP COLUMN is_alive,
    ADD COLUMN unit_state varchar (255);