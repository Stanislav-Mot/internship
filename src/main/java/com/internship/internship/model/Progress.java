package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Progress { // зачем отдельное энтити, если у тебя тут одно бизнес-поле?

    private Long id;
    private Task task;
    private Short percents;

    public Progress(Long id) {
        this.id = id;
    }
}
