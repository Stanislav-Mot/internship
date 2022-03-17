package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Task extends AssignmentImpl implements Assignment{

    @Column(nullable = false)
    private String name;
    private LocalDateTime startTime;
    private String description;
    private Integer estimate;
    private Integer progress;
    private Integer spentTime;
    private Integer priority;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assignment",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assignment",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> persons = new ArrayList<>();
}
