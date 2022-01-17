package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group implements Assignment {

    private Long id;
    private String name;
    private List<Assignment> tasks;
    private List<Person> person;

    public Group(Long id) {
        this.id = id;
    }

    public void add(Assignment task) {
        tasks.add(task);
    }

    public void addAll(List<? extends Assignment> taskList) {
        tasks.addAll(taskList);
    }

    public Integer size() {
        return tasks.size();
    }
}
