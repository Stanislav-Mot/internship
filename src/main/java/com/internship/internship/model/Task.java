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
public class Task implements Assignment {

    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private LocalDateTime startTime;
    private String description;
    private Integer estimate;
    private Integer progress;
    private Integer spentTime;
    private Integer priority;

    @ManyToMany
    @JoinTable(
            name = "group_task",
            joinColumns = @JoinColumn(name = "id_task"),
            inverseJoinColumns = @JoinColumn(name = "id_group"))
    private List<Group> groups = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "person_task",
            joinColumns = @JoinColumn(name = "id_task"),
            inverseJoinColumns = @JoinColumn(name = "id_person")
    )
    private List<Person> persons = new ArrayList<>();
}
