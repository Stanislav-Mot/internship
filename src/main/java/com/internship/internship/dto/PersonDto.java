package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDto {

    @Schema(example = "1")
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "Denis")
    @NotNull(message = "firstName should be not null", groups = {Transfer.New.class})
    private String firstName;

    @Schema(example = "Denisov")
    @NotNull(message = "lastName should be not null", groups = {Transfer.New.class})
    private String lastName;

    @Schema(example = "23")
    @NotNull(message = "age should be not null", groups = {Transfer.New.class})
    private Integer age;

    private List<GroupDto> groups;

    public PersonDto(Long id) {
        this.id = id;
    }
}

