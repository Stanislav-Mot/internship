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
    @NotNull(message = "priority id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "4")
    @NotNull(message = "taskId should be not null", groups = {Transfer.New.class})
    private Long taskId;

    @Schema(example = "84")
    @NotNull(message = "priority should be not null", groups = {Transfer.New.class, Transfer.Update.class})
    private Integer priority;
}
