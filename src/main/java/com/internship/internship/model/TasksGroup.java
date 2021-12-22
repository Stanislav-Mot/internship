package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TasksGroup {

    private Long id;
    private List<Task> tasks;
    private Person person;

}
