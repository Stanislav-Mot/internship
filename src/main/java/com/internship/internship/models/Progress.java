package com.internship.internship.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Task task;
    private Short percents;

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
