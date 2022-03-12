CREATE SEQUENCE BIGSERIAL START 101;

CREATE TABLE person
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(256),
    last_name  VARCHAR(256),
    birthdate  DATE
);

CREATE TABLE groups
(
    id        INT8 PRIMARY KEY DEFAULT nextval('BIGSERIAL'),
    name      VARCHAR(256) NOT NULL,
    id_parent INT8,
    FOREIGN KEY (id_parent) REFERENCES groups (id)
);

CREATE TABLE task
(
    id          INT8 PRIMARY KEY DEFAULT nextval('BIGSERIAL'),
    name        VARCHAR(256) NOT NULL,
    start_time  TIMESTAMP,
    description VARCHAR(256),
    estimate    INT,
    spent_time  INT,
    priority    INT,
    progress    INT
);

CREATE TABLE person_group
(
    id_person INT8,
    id_group  INT8,
    PRIMARY KEY (id_person, id_group),
    FOREIGN KEY (id_person) REFERENCES person (id),
    FOREIGN KEY (id_group) REFERENCES groups (id)
);

CREATE TABLE group_task
(
    id_task  INT8,
    id_group INT8,
    PRIMARY KEY (id_task, id_group),
    FOREIGN KEY (id_task) REFERENCES task (id),
    FOREIGN KEY (id_group) REFERENCES groups (id)
);