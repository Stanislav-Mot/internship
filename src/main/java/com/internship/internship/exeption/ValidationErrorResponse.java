package com.internship.internship.exeption;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Hidden
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();
}
