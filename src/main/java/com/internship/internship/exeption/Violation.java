package com.internship.internship.exeption;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;

@Hidden
@AllArgsConstructor
@Data
public class Violation {
    private final String fieldName;
    private final String message;
}
