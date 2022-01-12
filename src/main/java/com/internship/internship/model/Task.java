package com.internship.internship.model;

import com.internship.internship.model.Composite.ParentTask;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class Task implements ParentTask {

    private Long id;
    private String name;
    private String startTime;
    private String description;
    private LocalTime estimate;
    private LocalTime spentTime;

    private Person person;
    private Progress progress;
    private List<Group> groupsList;

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }

    public Task(Long id, String name, String startTime, Person person, Progress progress, List<Group> groupsList) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.person = person;
        this.progress = progress;
        this.groupsList = groupsList;
    }

    public Task(Long id, String name, String startTime, String description, LocalTime estimate, LocalTime spentTime, Person person, Progress progress, List<Group> groupsList) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.description = description;
        this.estimate = estimate;
        this.spentTime = spentTime;
        this.person = person;
        this.progress = progress;
        this.groupsList = groupsList;
    }
}
