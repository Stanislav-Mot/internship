package com.internship.internship.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Group groupTasks;

    public Person() {
    }

    public Person(long id, long idGroups, String firstName, String lastName, Integer age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groupTasks = new Group(idGroups);
    }

    public Person(Long id_person) {
        this.id = id_person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Group getGroupTasks() {
        return groupTasks;
    }

    public void setGroupTasks(Group groupTasks) {
        this.groupTasks = groupTasks;
    }
}
