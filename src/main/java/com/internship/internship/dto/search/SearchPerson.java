package com.internship.internship.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchPerson {

    @Schema(example = "Denis")
    private String firstName;

    @Schema(example = "Denisov")
    private String lastName;

    @Schema(example = "33")
    @Range(min = 0, max = 150)
    private Integer exactAge;

    @Schema(example = "55")
    @Range(min = 0, max = 150)
    private Integer rangeAge;
}