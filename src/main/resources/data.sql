INSERT INTO person (id, firstname, lastname, age)
VALUES (1, 'Ivan', 'Ivanov', 23),
       (2, 'Denis', 'Denisov', 15);

INSERT INTO groupOfTasks (id, name)
VALUES (1, 'asda'),
       (3, 'dfgsdfg');

INSERT INTO task (id, name, start_time)
VALUES (2, 'aaa', '2021-06-09'),
       (4, 'so0me', '2012-06-09');

INSERT INTO task_group (id_task, id_group)
VALUES (2, 1),
       (2, 3),
       (4, 3);

UPDATE groupOfTasks
SET id_person = 5
where id = 1;

UPDATE groupOfTasks
SET id_person = 6
where id = 3;

insert into task_group (id_group, id_task)
values (1, 2),
       (3, 4);

insert into progress (id, id_task, percents)
values (1, 2, 0),
       (2, 4, 0);

update task
set id_progress = 1
where id = 2;

update task
set id_progress = 2
where id = 4;

