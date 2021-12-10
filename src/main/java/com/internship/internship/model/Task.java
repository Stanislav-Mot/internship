package com.internship.internship.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Array;
import java.util.List;

@Entity
public class Task {

    @Id
    private Long id;
    private String name;
    private String startTime;
    private Person person;
    private Progress progress;
    private List<TasksGroup> groupTasksList;

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }

    public Task(Long id, String name, String startTime, Long personId, Long progressId, Array groupTasksIds) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.person = null;
        this.progress = null;
        this.groupTasksList = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public Person getPerson() {
        return person;
    }

    public Progress getProgress() {
        return progress;
    }

    public List<TasksGroup> getGroupTasksList() {
        return groupTasksList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public void setGroupTasksArrayList(List<TasksGroup> groupTasksList) {
        this.groupTasksList = groupTasksList;
    }
}
