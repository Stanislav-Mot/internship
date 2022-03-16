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
@Entity(name = "groups")
public class Group implements Assignment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

//    @OneToMany(targetEntity = Task.class, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "group_task",
//            joinColumns = @JoinColumn(name = "id_group"),
//            inverseJoinColumns = @JoinColumn(name = "id_task"))
//    private List<AssignmentImpl> tasks = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "id_parent")
//    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "general_connections",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    List<Person> persons = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

//    public void add(AssignmentImpl task) {
//        tasks.add(task);
//    }
//
//    public Integer size() {
//        return tasks.size();
//    }
}
