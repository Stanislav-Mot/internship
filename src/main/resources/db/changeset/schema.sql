DROP TABLE IF EXISTS group_of_tasks CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS progress CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS task_group CASCADE;
DROP TABLE IF EXISTS priority_of_task CASCADE;
DROP TABLE IF EXISTS group_in_group CASCADE;

CREATE TABLE person
(
    id        INT8 NOT NULL PRIMARY KEY,
    firstname VARCHAR(256),
    lastname  VARCHAR(256),
    age       SMALLINT
);

CREATE TABLE group_of_tasks
(
    id        INT8 NOT NULL PRIMARY KEY,
    name      VARCHAR(256),
    priority  boolean,
    id_person INT8,
    FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE
);

CREATE TABLE task
(
    id          INT8 NOT NULL PRIMARY KEY,
    name        VARCHAR(256),
    start_time  DATE,
    description VARCHAR(256),
    estimate    time,
    spent_time  time,
    id_person   INT8,
    id_progress INT8,
    FOREIGN KEY (id_person) REFERENCES person (id)
);

CREATE TABLE progress
(
    id       INT8 NOT NULL PRIMARY KEY,
    id_task  INT8,
    percents INT8,
    FOREIGN KEY (id_task) REFERENCES task (id) ON DELETE CASCADE
);

CREATE TABLE task_group
(
    id_task  INT8,
    id_group INT8,
    PRIMARY KEY (id_task, id_group),
    FOREIGN KEY (id_task) REFERENCES task (id) ON DELETE CASCADE,
    FOREIGN KEY (id_group) REFERENCES group_of_tasks (id) ON DELETE CASCADE
);

create table priority_of_task
(
    id       INT8 NOT NULL PRIMARY KEY,
    id_group INT8 NOT NULL,
    id_task  INT8,
    priority SMALLINT,
    FOREIGN KEY (id_group) REFERENCES group_of_tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (id_task) REFERENCES task (id) ON DELETE CASCADE
);

create table group_in_group
(
    id_parent INT8 NOT NULL,
    id_child  INT8 NOT NULL,
    PRIMARY KEY (id_parent, id_child),
    FOREIGN KEY (id_parent) REFERENCES group_of_tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (id_child) REFERENCES group_of_tasks (id) ON DELETE CASCADE
);