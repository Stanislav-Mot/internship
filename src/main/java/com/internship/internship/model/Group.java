package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group implements Assignment {

    public Group(Long id) {
        this.id = id;
    }

    private Long id;
    private String name;
    private List<Assignment> tasks;
    private List<Person> persons;

    public Group(Long id) {
        this.id = id;
    }

    public Group(String name) {
        this.name = name;
    }

    public void add(Assignment task) {
        tasks.add(task);
    }

    public Integer size() {
        return tasks.size();
    }
}
