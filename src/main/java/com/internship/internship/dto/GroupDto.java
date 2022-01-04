package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {

    @Schema()
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;
    @NotBlank(groups = {Transfer.New.class}, message = "name should be not null")
    private String name;
    @Null(groups = {Transfer.New.class, Transfer.Update.class})
    private List<TaskDto> tasks;
    private PersonDto person;

    public GroupDto(Long id) {
        this.id = id;
    }
}
