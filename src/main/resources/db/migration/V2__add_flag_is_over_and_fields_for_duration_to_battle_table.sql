ALTER TABLE battle
    ADD COLUMN is_over boolean,
    ADD COLUMN start_at timestamp,
    ADD COLUMN end_at timestamp;