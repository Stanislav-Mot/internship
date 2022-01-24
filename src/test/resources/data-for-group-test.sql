INSERT INTO person (id, firstname, lastname, age)
VALUES (1, 'GetTester', 'Tester', 15),
       (2, 'UpdateTester', 'Tester', 15),
       (3, 'DeleteGroupTester', 'Tester', 15),
       (4, 'DeleteTester', 'Tester', 15);

INSERT INTO group_of_tasks (id, name, id_person)
VALUES (1, 'testGroup', 1),
       (3, 'second', 3),
       (4, 'second', 1);

INSERT INTO task (id, name, start_time, id_group)
VALUES (2, 'cleaning', '2021-06-09', 1),
       (4, 'to_swim', '2012-06-09', 3),
       (9999, 'do_something', '2012-05-06'),
       (8888, 'for_delete', '2011-11-11', 1);

UPDATE group_of_tasks
set id_parent = 1
where id = 3;