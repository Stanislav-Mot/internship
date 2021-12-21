package com.internship.internship.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Progress {

    @Id
    private Long id;
    private Task task;
    private Short percents;

    public Progress() {
    }

    public Progress(Long id, Task task, Short percents) {
        this.id = id;
        this.task = task;
        this.percents = percents;
    }

    public Progress(Long progressId) {
        this.id = progressId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Short getPercents() {
        return percents;
    }

    public void setPercents(Short percents) {
        this.percents = percents;
    }
}
