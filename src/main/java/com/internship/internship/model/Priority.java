package com.internship.internship.model;

import lombok.Data;

@Data
public class Priority {
    private Long id;
    private Group group;
    private Task task;
    private Integer priority;

    public Priority() {
    }

    public Priority(Long id) {
        this.id = id;
    }

    public Priority(Long id, Group group, Task task, Integer priority) {
        this.id = id;
        this.group = group;
        this.task = task;
        this.priority = priority;
    }
}
