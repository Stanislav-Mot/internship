package com.internship.internship.dto;

import com.internship.internship.model.Assignment;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDto implements Assignment {

    @Schema(example = "4")
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "Cleaning")
    @NotNull(message = "id should be not null", groups = {Transfer.New.class})
    private String name;

    @Schema(example = "2012-06-09")
    private LocalDateTime startTime;

    private String description;
    private Integer progress;
    private Integer priority;
    private Integer estimate;
    private Integer spentTime;
    private PersonDto person;

    public TaskDto(Long id) {
        this.id = id;
    }
}