package com.internship.internship.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String start_time;
    private Person person;
    private Progress progress;
    private ArrayList<GroupTasks> groupTasksArrayList;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart_time() {
        return start_time;
    }

    public Person getPerson() {
        return person;
    }

    public Progress getProgress() {
        return progress;
    }

    public ArrayList<GroupTasks> getGroupTasksArrayList() {
        return groupTasksArrayList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public void setGroupTasksArrayList(ArrayList<GroupTasks> groupTasksArrayList) {
        this.groupTasksArrayList = groupTasksArrayList;
    }
}
