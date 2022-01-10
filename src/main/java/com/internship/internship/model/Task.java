package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    private Long id;
    private String name;
    private String startTime;
    private Person person;
    private Progress progress;
    private List<Group> groupsList;
    public Task(Long id) {
        this.id = id;
    }
}
