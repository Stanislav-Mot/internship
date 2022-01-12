package com.internship.internship.model;

import lombok.Data;

@Data
public class Progress {

    private Long id;
    private Task task;
    private Short percents;

    public Progress() {
    }

    public Progress(Long id) {
        this.id = id;
    }

    public Progress(Long id, Task task, Short percents) {
        this.id = id;
        this.task = task;
        this.percents = percents;
    }
}
