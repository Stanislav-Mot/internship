INSERT INTO groups (id, id_task) VALUES
  (1,2), (3,4);

INSERT INTO tasks (id, name, start_time, id_group) VALUES
  (2, 'aaa', '2021-06-09',1), (4, 'so0me', '2012-06-09',3);

INSERT INTO tasks_groups (id_task, id_group) VALUES
  (2, 1), (4,3);

INSERT INTO persons (id, id_groups, firstname, lastname, age) VALUES
    (5,3,'Ivan', 'Ivanov', 23), (6,1,'Oleg', 'Olegovich', 15);

--CREATE TABLE persons (
--  id INT8 NOT NULL PRIMARY KEY,
--  id_groups INT8,
--  firstname VARCHAR(256),
--  lastname VARCHAR(256),
--  age SMALLINT
--);

--CREATE TABLE groups (
--  id INT8 NOT NULL  PRIMARY KEY,
--  id_person INT8,
--  id_task INT8
--);
--
--CREATE TABLE tasks (
--  id INT8 NOT NULL PRIMARY KEY,
--  name VARCHAR(256),
--  start_time DATE,
--  id_person INT8,
--  id_progress INT8,
--  id_group INT8
--
--);
--
--CREATE TABLE tasks_groups(
--    id_task INT8,
--    id_group INT8,
--    PRIMARY KEY (id_task, id_group)
--);

--select * from tasks join tasks_groups on tasks.id = tasks_groups.id_task join groups on groups.id = tasks_groups.id_group