package com.internship.internship.model;

import javax.persistence.Entity;
import java.sql.Array;
import java.util.List;

@Entity
public class Group {

    private Long id;
    private List<Task> tasks;
    private Person person;

    public Group() {
    }

    public Group(Long id, Array tasks, Long personId) {
        this.id = id;
        this.tasks = null;
        this.person = null;
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
}
