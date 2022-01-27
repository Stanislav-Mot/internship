package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task implements Assignment {

    private Long id;
    private String name;
    private LocalDateTime startTime;
    private String description;
    private Integer estimate;
    private Integer progress;
    private Integer spentTime;
    private Integer priority;

    public Task(Long id) {
        this.id = id;
    }

}
