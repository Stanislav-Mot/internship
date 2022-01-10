create table priority_of_task
(
    id_group INT8 NOT NULL PRIMARY KEY,
    id_task INT8,
    priority SMALLINT,
    FOREIGN KEY (id_group) REFERENCES groupOfTasks (id) ON DELETE CASCADE
);