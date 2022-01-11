package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriorityDto {

    @Schema(example = "3")
    @NotNull(message = "priority id should be not null", groups = {Transfer.PriorityUpdate.class, Transfer.PriorityAdd.class})
    private Long id;

    @NotNull(message = "groupDto should be not null", groups = {Transfer.PriorityAdd.class})
    private GroupDto group;

    @NotNull(message = "taskDto should be not null", groups = {Transfer.PriorityAdd.class})
    private TaskDto task;

    @Schema(example = "84")
    @NotNull(message = "priority should be not null", groups = {Transfer.PriorityAdd.class, Transfer.PriorityUpdate.class})
    private Integer priority;

    public PriorityDto(Long id) {
        this.id = id;
    }
}
