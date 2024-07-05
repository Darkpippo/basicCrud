package com.example.task.basic.crud.basicCrud.model.exceptions;

public class InvalidDepartmentDtoException extends RuntimeException {
    public InvalidDepartmentDtoException() {
        super("Invalid Department DTO");
    }
}
