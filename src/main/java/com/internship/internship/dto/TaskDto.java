package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDto {

    @Schema(example = "4")
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "Cleaning")
    @NotNull(message = "id should be not null", groups = {Transfer.New.class})
    private String name;

    @Schema(example = "2012-06-09")
    private String startTime;

    private PersonDto person;

    private ProgressDto progress;

    private List<GroupDto> groupsList;

    private String description;

    private LocalTime estimate;

    private LocalTime spentTime;

    public TaskDto(Long id) {
        this.id = id;
    }

    public TaskDto(Long id, String name, String startTime, PersonDto person, ProgressDto progress, List<GroupDto> groupsList) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.person = person;
        this.progress = progress;
        this.groupsList = groupsList;
    }
}