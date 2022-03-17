package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AssignmentImpl.class)
    private Set<Assignment> assignments = new HashSet<>();

    public Person(Long id) {
        this.id = id;
    }
}