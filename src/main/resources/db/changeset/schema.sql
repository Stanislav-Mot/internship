CREATE SEQUENCE serial START 101;

CREATE TABLE person
(
    id        SERIAL PRIMARY KEY,
    firstname VARCHAR(256),
    lastname  VARCHAR(256),
    birthdate DATE
);

CREATE TABLE group_of_tasks
(
    id        INT8 PRIMARY KEY DEFAULT nextval('serial'),
    name      VARCHAR(256) NOT NULL,
    id_parent INT8,
    FOREIGN KEY (id_parent) REFERENCES group_of_tasks (id)
);

CREATE TABLE task
(
    id          INT8 PRIMARY KEY DEFAULT nextval('serial'),
    name        VARCHAR(256) NOT NULL,
    start_time  TIMESTAMP,
    description VARCHAR(256),
    estimate    INT8,
    spent_time  INT8,
    priority    INT8,
    progress    INT8,
    id_group    INT8,
    FOREIGN KEY (id_group) REFERENCES group_of_tasks (id)
);

CREATE TABLE person_group
(
    id_person INT8,
    id_group  INT8,
    PRIMARY KEY (id_person, id_group),
    FOREIGN KEY (id_person) REFERENCES person (id),
    FOREIGN KEY (id_group) REFERENCES group_of_tasks (id)
)