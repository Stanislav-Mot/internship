package com.internship.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDto {


    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<GroupDto> groups;

    public PersonDto(Long id) {
        this.id = id;
    }
}

