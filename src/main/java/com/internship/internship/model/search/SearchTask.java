package com.internship.internship.model.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTask {

    @Schema(example = "Cleaning")
    private String name;

    @Schema(example = "2012-06-09")
    private LocalDateTime minStartTime;

    @Schema(example = "2021-12-12")
    private LocalDateTime maxStartTime;

    @Schema(example = "0")
    @Range(min = 0, max = 100)
    private Short fromProgress;

    @Schema(example = "100")
    @Range(min = 0, max = 100)
    private Short toProgress;

    public SearchTask(String name, LocalDateTime minStartTime, LocalDateTime maxStartTime) {
        this.name = name;
        this.minStartTime = minStartTime;
        this.maxStartTime = maxStartTime;
    }
}
