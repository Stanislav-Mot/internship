package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {

    public Group(Long id) {
        this.id = id;
    }

    private Long id;
    private String name;
    private List<Task> tasks;
    private Person person;

}
