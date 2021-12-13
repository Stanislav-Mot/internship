package com.internship.internship.model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Group {

    private Long id;
    private String name;
    private List<Task> tasks;
    private Person person;

    public Group() {}

    public Group(Long id, String name, List<Task> tasks, Person person) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.person = person;
    }

    public Group(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
