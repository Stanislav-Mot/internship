package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "group_of_tasks")
public class Group implements Assignment{

    @Id
    private Long id;
    private String name;

    @ManyToMany(targetEntity = Task.class)
    @JoinTable(
            name = "group_task",
            joinColumns = @JoinColumn(name = "id_group"),
            inverseJoinColumns = @JoinColumn(name = "id_task"))
    private List<Assignment> tasks = new ArrayList<>();

    @ManyToOne
    private Group group;

    @ManyToMany()
    @JoinTable(
            name = "person_group",
            joinColumns = @JoinColumn(name = "id_group"),
            inverseJoinColumns = @JoinColumn(name = "id_person"))
    private List<Person> persons;


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
