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

    public Progress(Long id, Long taskId, Short percents) {
        this.id = id;
        this.task = new Task(taskId);
        this.percents = percents;
    }

    public Long getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public Short getPercents() {
        return percents;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setPercents(Short percents) {
        this.percents = percents;
    }
}
