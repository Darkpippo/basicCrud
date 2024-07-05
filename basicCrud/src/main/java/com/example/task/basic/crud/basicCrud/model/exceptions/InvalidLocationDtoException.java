package com.example.task.basic.crud.basicCrud.model.exceptions;

public class InvalidLocationDtoException extends RuntimeException {
    public InvalidLocationDtoException() {
        super("Invalid Location DTO");
    }
}
