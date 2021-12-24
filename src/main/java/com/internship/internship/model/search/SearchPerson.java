package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchPerson {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer exactAge;
    private Integer rangeAge;
}