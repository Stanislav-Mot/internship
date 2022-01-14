package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task implements Assignment {

    private Long id;
    private String name;
    private String startTime;
    private String description;
    private Integer estimate;
    private LocalTime spentTime;
    private Short progress;
    private Integer priority;
    private Person person;

    public Task(Long id) {
        this.id = id;
    }
}
