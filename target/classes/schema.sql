DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS persons CASCADE;
DROP TABLE IF EXISTS progresses CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS tasks_groups CASCADE;

CREATE TABLE groups (
  id INT8 NOT NULL  PRIMARY KEY,
  id_person INT8,
  id_task INT8
);

CREATE TABLE persons (
  id INT8 NOT NULL PRIMARY KEY,
  id_groups INT8,
  firstname VARCHAR(256),
  lastname VARCHAR(256),
  age SMALLINT
);

CREATE TABLE progresses (
  id INT8 NOT NULL PRIMARY KEY,
  id_task INT8,
  percents INT8
);

CREATE TABLE tasks (
  id INT8 NOT NULL PRIMARY KEY,
  name VARCHAR(256),
  start_time DATE,
  id_person INT8,
  id_progress INT8,
  id_group INT8

);

CREATE TABLE tasks_groups(
    id_task INT8,
    id_group INT8,
    PRIMARY KEY (id_task, id_group)
);

ALTER TABLE tasks_groups
    ADD CONSTRAINT fk_tasks_groups_1 FOREIGN KEY (id_task) REFERENCES tasks(id),
    ADD CONSTRAINT fk_tasks_groups_2 FOREIGN KEY (id_group) REFERENCES groups(id);

ALTER TABLE groups
    ADD CONSTRAINT fk_person_group FOREIGN KEY (id_person) REFERENCES persons(id);
--    ADD CONSTRAINT fk_tg_groups FOREIGN KEY (id_task) REFERENCES tasks_groups(id_task);

ALTER TABLE persons
    ADD CONSTRAINT fk_groups FOREIGN KEY (id_groups) REFERENCES groups(id);

ALTER TABLE progresses
    ADD CONSTRAINT fk_person FOREIGN KEY (id_task) REFERENCES tasks(id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_person_task FOREIGN KEY (id_person) REFERENCES persons(id),
    ADD CONSTRAINT fk_progress_task FOREIGN KEY (id_progress) REFERENCES progresses(id);
--    ADD CONSTRAINT fk_tg_task FOREIGN KEY (id_group) REFERENCES tasks_groups (id_group);


