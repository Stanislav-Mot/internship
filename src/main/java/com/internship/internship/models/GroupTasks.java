package com.internship.internship.models;

import java.util.LinkedList;

public class GroupTasks {

    private Long id;
    private LinkedList<Task> tasks;
    private Person person;

    public GroupTasks(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(LinkedList<Task> tasks) {
        this.tasks = tasks;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
