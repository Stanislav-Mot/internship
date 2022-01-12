INSERT INTO person (id, firstname, lastname, age)
VALUES (1, 'Ivan', 'Ivanov', 23),
       (2, 'Kolya', 'Denisov', 15);

INSERT INTO groupOfTasks (id, name, priority)
VALUES (1, 'Programming', false),
       (3, 'Build', false);

INSERT INTO task (id, name, start_time)
VALUES (2, 'Build something', '2021-06-09'),
       (4, 'Fishing', '2012-06-09');

INSERT INTO task_group (id_task, id_group)
VALUES (2, 1),
       (2, 3),
       (4, 3);

UPDATE groupOfTasks
SET id_person = 1
where id = 1;

UPDATE groupOfTasks
SET id_person = 2
where id = 3;

insert into progress (id, id_task, percents)
values (1, 2, 0),
       (2, 4, 0);

update task
set id_progress = 1
where id = 2;

update task
set id_progress = 2
where id = 4;


INSERT INTO person (id, firstname, lastname, age)
VALUES (1111, 'Denis', 'Denisov', 23),
       (2222, 'Tester', 'Testerovich', 0);

INSERT INTO groupOfTasks (id, name, priority)
VALUES (11, 'ForDenis', true),
       (22, 'ForTester', false),
       (33, 'ForDenisGroup', false);

UPDATE groupOfTasks
SET id_person = 1111
where id = 11;
UPDATE groupOfTasks
SET id_person = 2222
where id = 22;

INSERT INTO group_in_group (id_parent, id_child)
VALUES (11, 33);

INSERT INTO task (id, name, start_time)
VALUES (21, 'cleaning', '2021-06-09'),
       (22, 'to_swim', '2012-06-09'),
       (23, 'do_something', '2012-05-06'),
       (24, 'for_delete', '2011-11-11');

INSERT INTO task_group (id_task, id_group)
VALUES (21, 11),
       (22, 11),
       (23, 11),
       (24, 22);

INSERT INTO priority_of_task (id, id_task, id_group, priority)
VALUES (111, 21, 11, 4),
       (222, 22, 11, 92),
       (333, 23, 11, 35);

insert into progress (id, id_task, percents)
values (98, 22, 54),
       (97, 23, 55);

update task
set id_progress = 98
where id = 22;
update task
set id_progress = 97
where id = 23;
