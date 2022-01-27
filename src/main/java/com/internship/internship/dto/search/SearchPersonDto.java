package com.internship.internship.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPersonDto {
    @Schema(description = "Person name", example = "Denis")
    private String firstName;

    @Schema(description = "Person surname", example = "Denisov")
    private String lastName;

    @Schema(description = "Person age", example = "33")
    @Max(value = 150, message = "exactAge should be between 0 and 150")
    @Min(value = 0, message = "exactAge should be between 0 and 150")
    private Integer exactAge;

    @Schema(description = "If you want search in a range age, set start", example = "1")
    @Max(value = 150, message = "rangeAgeStart should be between 0 and 150")
    @Min(value = 0, message = "rangeAgeStart should be between 0 and 150")
    private Integer rangeAgeStart;

    @Schema(description = "If you want search in a range age, set end", example = "99")
    @Max(value = 150, message = "rangeAgeEnd should be between 0 and 150")
    @Min(value = 0, message = "rangeAgeEnd should be between 0 and 150")
    private Integer rangeAgeEnd;
}