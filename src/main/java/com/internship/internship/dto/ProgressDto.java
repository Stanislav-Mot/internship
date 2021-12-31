package com.internship.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgressDto {

    private Long id;
    private TaskDto task;
    private Short percents;

    public ProgressDto(Long id) {
        this.id = id;
    }
}

