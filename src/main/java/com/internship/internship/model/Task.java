package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    private Long id;
    private String name;
    private String startTime;
    private String description;
    private LocalTime estimate;
    private LocalTime spentTime;

    private Person person;
    private Progress progress;
    private List<Group> groupsList;

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
}
