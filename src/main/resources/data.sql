INSERT INTO groups (id) VALUES
        (1), (3);

INSERT INTO tasks (id, name, start_time) VALUES
    (2, 'aaa', '2021-06-09'), (4, 'so0me', '2012-06-09');

INSERT INTO tasks_groups (id_task, id_group) VALUES
    (2, 1), (2,3), (4,3);

INSERT INTO persons (id, id_groups, firstname, lastname, age) VALUES
    (5,3,'Ivan', 'Ivanov', 23), (6,1,'Oleg', 'Olegovich', 15);

UPDATE groups SET id_person = 5 where id = 1;
UPDATE groups SET id_person = 6 where id = 3;

