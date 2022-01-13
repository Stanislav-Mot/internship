INSERT INTO group_of_tasks (id, name)
VALUES (1, 'one'),
       (2, 'two'),
       (3, 'three'),
       (4, 'four');

INSERT INTO task (id, name, start_time)
VALUES (11, 'cleaning', '2021-06-09'),
       (22, 'to_swim', '2012-06-09'),
       (33, 'do_something', '2012-05-06'),
       (44, 'for_delete', '2011-11-11'),
       (55, 'for_progress', '2011-11-11'),
       (66, 'for_progress', '2011-11-11');

INSERT INTO task_group (id_group, id_task)
VALUES (1, 11),
       (2, 22),
       (3, 33),
       (4, 44),
       (4, 55),
       (4, 66);

INSERT INTO priority_of_task (id, id_task, id_group, priority)
VALUES (111, 11, 1, 91),
       (222, 22, 2, 92),
       (333, 33, 3, 93),
       (444, 44, 4, 96),
       (555, 55, 4, 95);