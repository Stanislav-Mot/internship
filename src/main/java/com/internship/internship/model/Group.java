package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Target;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "groups")
public class Group extends AssignmentImpl implements Assignment{

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @Target(Group.class)
    private List<Assignment> assignments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assignment",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> persons = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }
}
