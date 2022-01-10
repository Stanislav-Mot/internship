package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {

    private Long id;
    private String name;
    private List<Task> tasks;
    private Person person;

    private boolean priority;

    /**
     *List of ids regulating task priority
     */
    private ArrayList<PriorityTask> priorityTasks;

    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id, String name, List<Task> tasks, Person person) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.person = person;
    }
}
