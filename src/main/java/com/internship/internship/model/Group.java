package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {

    private Long id;
    private String name;
    private List<Task> tasks;
    private Person person;
    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id) {
        this.id = id;
    }

}
