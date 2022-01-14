INSERT INTO person (id, firstname, lastname, age)
VALUES (1, 'Ivan', 'Ivanov', 23),
       (2, 'Kolya', 'Denisov', 15);

INSERT INTO group_of_tasks (id, name, priority)
VALUES (1, 'Programming GROUp', false),
       (3, 'Build GROUP', false);

INSERT INTO task (id, name, start_time, id_group)
VALUES (2, 'Build something TASK', '2021-06-09', 1),
       (4, 'Fishing TASK', '2012-06-09', 3);


UPDATE group_of_tasks
SET id_person = 1
where id = 1;

UPDATE group_of_tasks
SET id_person = 2
where id = 3;

update task
set id_progress = 1
where id = 2;

update task
set id_progress = 2
where id = 4;


INSERT INTO person (id, firstname, lastname, age)
VALUES (1111, 'Denis', 'Denisov', 23),
       (2222, 'Tester', 'Testerovich', 0);

INSERT INTO group_of_tasks (id, name, priority)
VALUES (11, 'ForDenis GROUP', true),
       (22, 'ForTester GROUP', false),
       (44, 'for_33_task GROUP', false),
       (33, 'ForDenisGroup GROUP', false),
       (55, '55 GROUP', false);

UPDATE group_of_tasks
SET id_person = 1111
WHERE id = 11;

UPDATE group_of_tasks
SET id_person = 2222
WHERE id = 22;

UPDATE group_of_tasks
SET id_parent = 11
WHERE id = 33;

UPDATE group_of_tasks
SET id_parent = 33
WHERE id = 44;

UPDATE group_of_tasks
SET id_parent = 44
WHERE id = 55;

INSERT INTO task (id, name, start_time, id_group)
VALUES (21, 'cleaning TASK', '2021-06-09', 11),
       (22, 'to_swim TASK', '2012-06-09', 11),
       (23, 'do_something TASK', '2012-05-06', 11),
       (24, 'for_delete TASK', '2011-11-11', 22),
       (26, 'for_44_group TASK', '2011-11-11', 44),
       (25, 'for_33_group TASK', '2012-05-06', 33);

update task
set id_progress = 98
where id = 22;
update task
set id_progress = 97
where id = 23;
