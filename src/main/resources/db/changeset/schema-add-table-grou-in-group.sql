create table group_in_group
(
    id_parent INT8 NOT NULL,
    id_child  INT8 NOT NULL,
    PRIMARY KEY (id_parent, id_child),
    FOREIGN KEY (id_parent) REFERENCES groupOfTasks (id) ON DELETE CASCADE,
    FOREIGN KEY (id_child) REFERENCES groupOfTasks (id) ON DELETE CASCADE
);