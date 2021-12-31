package com.internship.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {

    private Long id;
    private String name;
    private List<TaskDto> tasks;
    private PersonDto person;

    public GroupDto(Long id) {
        this.id = id;
    }
}
