CREATE SEQUENCE BIG_SERIAL START 101;

CREATE TABLE person
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(256),
    last_name  VARCHAR(256),
    birthdate  DATE,
    password   VARCHAR(256) NOT NULL,
    email      VARCHAR(256) NOT NULL
);

CREATE TABLE user_role
(
    user_id INT8 NOT NULL,
    role    varchar(255),
    FOREIGN KEY (user_id) REFERENCES person (id),
    PRIMARY KEY (user_id, role)
);

CREATE TABLE groups
(
    id   INT8 PRIMARY KEY DEFAULT nextval('BIG_SERIAL'),
    name VARCHAR(256) NOT NULL
);

CREATE TABLE task
(
    id          INT8 PRIMARY KEY DEFAULT nextval('BIG_SERIAL'),
    name        VARCHAR(256) NOT NULL,
    start_time  TIMESTAMP,
    description VARCHAR(256),
    estimate    INT,
    spent_time  INT,
    priority    INT,
    progress    INT
);

CREATE TABLE assignment
(
    group_id  INT8,
    child_id  INT8,
    person_id INT8,
    task_id   INT8,
    PRIMARY KEY (group_id)
);

ALTER TABLE IF EXISTS assignment
    ADD CONSTRAINT ass_group_id_groups FOREIGN KEY (group_id) REFERENCES groups;
ALTER TABLE IF EXISTS assignment
    ADD CONSTRAINT ass_person_id_person FOREIGN KEY (person_id) REFERENCES person;
ALTER TABLE IF EXISTS assignment
    ADD CONSTRAINT ass_task_id_task FOREIGN KEY (task_id) REFERENCES task;
ALTER TABLE IF EXISTS assignment
    ADD CONSTRAINT ass_child_id_groups FOREIGN KEY (child_id) REFERENCES groups;