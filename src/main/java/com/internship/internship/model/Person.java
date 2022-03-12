package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Person {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private User user;

    @ManyToMany(targetEntity = Group.class)
    @JoinTable(
            name = "person_group",
            joinColumns = @JoinColumn(name = "id_person"),
            inverseJoinColumns = @JoinColumn(name = "id_group"))
    private List<Assignment> groups = new ArrayList<>();

    public Person(Long id) {
        this.id = id;
    }
}