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
public class PersonDto {


    @NotNull(message = "id should be not null", groups = {Transfer.Update.class})
    private Long id;

    @NotNull(message = "firstName should be not null", groups = {Transfer.New.class})
    private String firstName;

    @NotNull(message = "lastName should be not null", groups = {Transfer.New.class})
    private String lastName;

    @NotNull(message = "age should be not null", groups = {Transfer.New.class})
    private Integer age;

    private List<GroupDto> groups;

    public PersonDto(Long id) {
        this.id = id;
    }
}

