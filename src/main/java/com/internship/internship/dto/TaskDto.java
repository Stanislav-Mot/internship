package com.internship.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDto {

    private Long id;
    private String name;
    private String startTime;
    private PersonDto person;
    private ProgressDto progress;
    private List<GroupDto> groupsList;

    public TaskDto(Long id) {
        this.id = id;
    }

}