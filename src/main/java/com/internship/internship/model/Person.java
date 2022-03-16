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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "persons")
    private List<AssignmentImpl> assignments = new ArrayList<>();

    public Person(Long id) {
        this.id = id;
    }
}