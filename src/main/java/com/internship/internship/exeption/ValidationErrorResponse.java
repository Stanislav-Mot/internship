package com.internship.internship.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();
}
