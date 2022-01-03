package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTask {
    private String name;
    private LocalDateTime minStartTime;
    private LocalDateTime maxStartTime;

    @Range(min = 0, max = 100)
    private Short fromProgress;

    @Range(min = 0, max = 100)
    private Short toProgress;

    public SearchTask(String name, LocalDateTime minStartTime, LocalDateTime maxStartTime) {
        this.name = name;
        this.minStartTime = minStartTime;
        this.maxStartTime = maxStartTime;
    }
}
