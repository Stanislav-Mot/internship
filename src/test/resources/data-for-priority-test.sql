INSERT INTO group_of_tasks (id, name)
VALUES (1, 'one'),
       (2, 'two'),
       (3, 'three'),
       (4, 'four');

INSERT INTO task (id, name, start_time, id_group)
VALUES (11, 'cleaning', '2021-06-09', 1),
       (22, 'to_swim', '2012-06-09', 2),
       (33, 'do_something', '2012-05-06', 3),
       (44, 'for_delete', '2011-11-11', 4),
       (55, 'for_progress', '2011-11-11'),
       (66, 'for_progress', '2011-11-11');