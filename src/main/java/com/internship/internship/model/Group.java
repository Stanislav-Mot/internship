package com.internship.internship.model;

import com.internship.internship.model.Composite.CompositeTask;
import com.internship.internship.model.Composite.ParentTask;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Group implements ParentTask {

    private Long id;
    private String name;
    private CompositeTask tasks; // давай назовем это assignment
    private Person person;
    private boolean priority; // почему булеан?

    /**
     * List of ids regulating task priority
     */
    private List<Priority> priorityList;

    public Group() {
    }

    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id, String name, CompositeTask tasks, Person person) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.person = person;
    }
}
