package com.internship.internship.exeption;

public class ChangesNotAppliedException extends RuntimeException {
    public ChangesNotAppliedException(String message) {
        super(message);
    }
}