package com.internship.internship.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTaskDto {
    @Schema(description = "Task name", example = "cleaning")
    private String name;

    @Schema(description = "Search from min progress", example = "0")
    @Min(value = 0, message = "fromProgress should be between 0 and 100")
    @Max(value = 100, message = "fromProgress should be between 0 and 100")
    private Integer fromProgress;

    @Schema(description = "Search to max progress", example = "100")
    @Min(value = 0, message = "toProgress should be between 0 and 100")
    @Max(value = 100, message = "toProgress should be between 0 and 100")
    private Integer toProgress;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Search from min start time", example = "2012-12-12T01:02:03")
    private String minStartTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Search from to start time", example = "2022-12-12T01:02:03")
    private String maxStartTime;
}
