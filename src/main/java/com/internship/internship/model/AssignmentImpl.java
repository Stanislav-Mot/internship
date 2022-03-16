package com.internship.internship.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "assignment")
@Entity
public class AssignmentImpl implements Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "general_connections",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    List<Group> groups = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "general_connections",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "general_connections",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    List<Person> persons = new ArrayList<>();

    @ManyToOne
    @JoinTable(
            name = "general_connections",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private AssignmentImpl assignment;
}
