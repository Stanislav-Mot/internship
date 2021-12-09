package com.internship.internship.models;

public class Person {

    private Long id;
    private String firstname;
    private String lastname;
    private Integer age;
    private GroupTasks groupTasks;

    public Person(long id, long id_groups, String firstname, String lastname, Integer age) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.groupTasks = new GroupTasks(id_groups);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public GroupTasks getGroupTasks() {
        return groupTasks;
    }

    public void setGroupTasks(GroupTasks groupTasks) {
        this.groupTasks = groupTasks;
    }
}
