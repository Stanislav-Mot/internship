package com.internship.internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Priority { // зачем отдельное энтити, если у тебя тут одно бизнес-поле?
    private Long id;
    private Group group;
    private Task task;
    private Integer priority;

    public Priority(Long id) {
        this.id = id;
    }
}
