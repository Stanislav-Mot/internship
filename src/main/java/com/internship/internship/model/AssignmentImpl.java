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
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AssignmentImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "assignment",
//            joinColumns = @JoinColumn(name = "group_id"),
//            inverseJoinColumns = @JoinColumn(name = "child_id"))
//    private List<Group> groups = new ArrayList<>();
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "assignment",
//            joinColumns = @JoinColumn(name = "group_id"),
//            inverseJoinColumns = @JoinColumn(name = "task_id"))
//    private List<Task> tasks = new ArrayList<>();
}
