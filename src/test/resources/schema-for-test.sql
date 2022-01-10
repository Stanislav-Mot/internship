DROP TABLE IF EXISTS groupOfTasks CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS progress CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS task_group CASCADE;
DROP TABLE IF EXISTS priority_of_task CASCADE;

CREATE TABLE person
(
    id        INT8 NOT NULL PRIMARY KEY,
    firstname VARCHAR(256),
    lastname  VARCHAR(256),
    age       SMALLINT
);

CREATE TABLE groupOfTasks
(
    id        INT8 NOT NULL PRIMARY KEY,
    id_person BIGINT,
    name      VARCHAR(256),
    FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE
);


CREATE TABLE progress
(
    id       INT8 NOT NULL PRIMARY KEY,
    id_task  INT8,
    percents INT8
);

CREATE TABLE task
(
    id          INT8 NOT NULL PRIMARY KEY,
    name        VARCHAR(256),
    start_time  DATE,
    id_person   INT8,
    id_progress INT8
);

CREATE TABLE task_group
(
    id_task  INT8,
    id_group INT8,
    PRIMARY KEY (id_task, id_group)
);

ALTER TABLE task_group
    ADD CONSTRAINT fk_task_group_1 FOREIGN KEY (id_task) REFERENCES task (id);

ALTER TABLE task_group
    ADD CONSTRAINT fk_task_group_2 FOREIGN KEY (id_group) REFERENCES groupOfTasks (id);

ALTER TABLE progress
    ADD CONSTRAINT fk_person FOREIGN KEY (id_task) REFERENCES task (id);

ALTER TABLE task
    ADD CONSTRAINT fk_person_task FOREIGN KEY (id_person) REFERENCES person (id);

ALTER TABLE task
    ADD COLUMN description VARCHAR(256);
ALTER TABLE task
    ADD COLUMN estimate time;
ALTER TABLE task
    ADD COLUMN spent_time time;

create table priority_of_task
(
    id       INT8 NOT NULL PRIMARY KEY,
    id_group INT8 NOT NULL,
    id_task  INT8 NOT NULL,
    priority SMALLINT,
    FOREIGN KEY (id_group) REFERENCES groupOfTasks (id) ON DELETE CASCADE,
    FOREIGN KEY (id_task) REFERENCES task (id) ON DELETE CASCADE
);