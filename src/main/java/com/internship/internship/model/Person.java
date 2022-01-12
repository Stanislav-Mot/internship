package com.internship.internship.model;

import lombok.Data;

import java.util.List;

@Data
public class Person {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Group> groups;

    public Person() {
    }

    public Person(Long id) {
        this.id = id;
    }

    public Person(Long id, String firstName, String lastName, Integer age, List<Group> groups) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groups = groups;
    }
}
