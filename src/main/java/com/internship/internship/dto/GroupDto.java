package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {

    @Schema(example = "2")
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "Home")
    @NotBlank(groups = {Transfer.New.class}, message = "name should be not null")
    private String name;

    @Null(groups = {Transfer.New.class, Transfer.Update.class})
    private List<TaskDto> tasks;

    private PersonDto person;

    private boolean priority;

    /**
     * List of ids regulating task priority
     */
    private ArrayList<PriorityDto> priorityDtos;

    public GroupDto(Long id) {
        this.id = id;
    }

    public GroupDto(Long id, String name, List<TaskDto> tasks, PersonDto person) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.person = person;
    }
}
