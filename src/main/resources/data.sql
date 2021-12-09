INSERT INTO groups (id, id_task) VALUES
    (1,2), (3,4);

INSERT INTO tasks (id, name, start_time, id_group) VALUES
    (2, 'aaa', '2021-06-09',1), (4, 'so0me', '2012-06-09',3);

INSERT INTO tasks_groups (id_task, id_group) VALUES
    (2, 1), (4,3);

INSERT INTO persons (id, id_groups, firstname, lastname, age) VALUES
    (5,3,'Ivan', 'Ivanov', 23), (6,1,'Oleg', 'Olegovich', 15);