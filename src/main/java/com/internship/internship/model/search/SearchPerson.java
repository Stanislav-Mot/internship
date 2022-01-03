package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchPerson {
    private String firstName;
    private String lastName;

    @Range(min = 0, max = 150)
    private Integer exactAge;

    @Range(min = 0, max = 150)
    private Integer rangeAge;
}