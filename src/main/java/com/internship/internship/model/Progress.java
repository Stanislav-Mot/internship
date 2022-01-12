package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Progress {

    private Long id;
    private Task task;
    private Short percents;

    public Progress(Long id) {
        this.id = id;
    }
}
