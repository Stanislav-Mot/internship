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
public class ProgressDto {

    @Schema(example = "3")
    @NotNull(message = "task should be not null", groups = {Transfer.Update.class})
    private Long id;

    @NotNull(message = "task should be not null", groups = {Transfer.New.class})
    private TaskDto task;

    @Schema(example = "84")
    @NotNull(message = "task should be not null", groups = {Transfer.New.class, Transfer.Update.class})
    private Short percents;

    public ProgressDto(Long id) {
        this.id = id;
    }
}

