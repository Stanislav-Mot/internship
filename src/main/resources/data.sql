INSERT INTO groups (id, name) VALUES
        (1, 'asda'), (3, 'dfgsdfg');

INSERT INTO tasks (id, name, start_time) VALUES
    (2, 'aaa', '2021-06-09'), (4, 'so0me', '2012-06-09');

INSERT INTO tasks_groups (id_task, id_group) VALUES
    (2, 1), (2,3), (4,3);

INSERT INTO persons (id, firstname, lastname, age) VALUES
    (5,'Ivan', 'Ivanov', 23), (6,'Oleg', 'Olegovich', 15);

UPDATE groups SET id_person = 5 where id = 1;
UPDATE groups SET id_person = 6 where id = 3;

