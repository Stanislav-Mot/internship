package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDto {

    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;
    @NotNull(message = "id should be not null", groups = {Transfer.New.class})
    private String name;
    private String startTime;
    private PersonDto person;
    private ProgressDto progress;
    private List<GroupDto> groupsList;

    public TaskDto(Long id) {
        this.id = id;
    }

}