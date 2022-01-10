ALTER TABLE task
    ADD COLUMN description VARCHAR(256),
    ADD COLUMN estimate    time,
    ADD COLUMN spent_time  time;