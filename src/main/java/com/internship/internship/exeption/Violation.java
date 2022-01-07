package com.internship.internship.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Violation {
    private final String fieldName;

    private final String message;
}
