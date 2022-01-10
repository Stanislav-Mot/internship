package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTask {
    private String name;
    private LocalDateTime minStartTime;
    private LocalDateTime maxStartTime;
    private Short fromProgress;
    private Short toProgress;

    public SearchTask(String name, LocalDateTime minStartTime, LocalDateTime maxStartTime) {
        this.name = name;
        this.minStartTime = minStartTime;
        maxStartTime = maxStartTime;
    }
}
