package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Group> groups;
    public Person(Long id) {
        this.id = id;
    }

    public Person(Long id) {
        this.id = id;
    }
}
