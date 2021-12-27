package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTask {
    private Long id;
    private String name;
    private String fromStartTime;
    private String ToStartTime;
    private Short fromProgress;
    private Short toProgress;

    public SearchTask(Long id, String name, String fromStartTime, String toStartTime) {
        this.id = id;
        this.name = name;
        this.fromStartTime = fromStartTime;
        ToStartTime = toStartTime;
    }
}
