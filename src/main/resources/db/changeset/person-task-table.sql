CREATE TABLE person_task
(
    id_task  INT8,
    id_person INT8,
    PRIMARY KEY (id_task, id_person),
    FOREIGN KEY (id_task) REFERENCES task (id),
    FOREIGN KEY (id_person) REFERENCES person (id)
);