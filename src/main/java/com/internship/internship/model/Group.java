package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "groups")
public class Group implements Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Fetch(value = FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assignment",
            joinColumns = @JoinColumn(name = "children_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "group_id", nullable = true))
    private List<Group> children = new ArrayList<>();

    @Fetch(value = FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assignment",
            joinColumns = @JoinColumn(name = "group_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "task_id", nullable = true))
    private List<Task> tasks = new ArrayList<>();
}
