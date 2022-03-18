package com.internship.internship.dto;

import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDto {
    @Schema(example = "1")
    @Null(message = "id should be generate be db", groups = {Transfer.New.class})
    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @Schema(example = "Denis")
    @NotNull(message = "firstName should be not null", groups = {Transfer.New.class, Transfer.Update.class})
    private String firstName;

    @Schema(example = "Denisov")
    @NotNull(message = "lastName should be not null", groups = {Transfer.New.class, Transfer.Update.class})
    private String lastName;

    @Schema(example = "23")
    @NotNull(message = "age should be not null", groups = {Transfer.New.class})
    private LocalDate birthdate;

    private List<GroupDto> assignments;
}

