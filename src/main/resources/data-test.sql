INSERT INTO groups (id, name) VALUES
        (1, 'first'), (3, 'second');

INSERT INTO tasks (id, name, start_time) VALUES
    (2, 'cleaning', '2021-06-09'), (4, 'to_swim', '2012-06-09');

INSERT INTO tasks_groups (id_task, id_group) VALUES
    (2, 1), (2,3), (4,3);

INSERT INTO persons (id, firstname, lastname, age) VALUES
    (9991,'Ivan', 'Ivanov', 23),
    (9992,'Tester', 'Tester', 15),
    (9993,'Tester', 'Tester',15);

UPDATE groups SET id_person = 5 where id = 1;
UPDATE groups SET id_person = 6 where id = 3;

