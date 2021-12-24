INSERT INTO persons (id, firstname, lastname, age) VALUES
    (1,'GetTester', 'Tester', 15),
    (2,'UpdateTester', 'Tester',15),
    (3,'DeleteGroupTester', 'Tester',15),
    (4,'DeleteTester', 'Tester',15);

INSERT INTO groups (id, name, id_person) VALUES
        (1, 'testGroup', 1),
        (3, 'second', 3),
        (4, 'second', 1);

INSERT INTO tasks (id, name, start_time) VALUES
    (2, 'cleaning', '2021-06-09'),
    (4, 'to_swim', '2012-06-09'),
    (9999, 'do_something', '2012-05-06'),
    (8888, 'for_delete', '2011-11-11');

INSERT INTO tasks_groups (id_task, id_group) VALUES
    (2, 1), (2,3), (4,3), (8888,1);


-- тестовые ресурсы должны быть не main/resources, а в test/resources