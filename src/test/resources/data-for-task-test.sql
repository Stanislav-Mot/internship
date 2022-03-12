INSERT INTO groups (name)
VALUES ('testGroup'),
       ('second'),
       ('second');

INSERT INTO task (name, start_time, id_group)
VALUES ('cleaning', '2021-06-09', 1),
       ('to_swim', '2012-06-09', 3),
       ('do_something', '2012-05-06', 2),
       ('for_delete', '2011-11-11', 1),
       ('for_progress', '2011-11-11', 2);