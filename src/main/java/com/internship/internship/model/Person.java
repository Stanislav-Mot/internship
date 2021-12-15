package com.internship.internship.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Person {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Group> groups;

    public Person() {
    }

    public Person(Long personId) {
        this.id = personId;
    }

    public Person(Long id, String firstName, String lastName, Integer age, List<Group> groups) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroupTasks(List<Group> groups) {
        this.groups = groups;
    }
}
