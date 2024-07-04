package com.example.task.basic.crud.basicCrud.model.exceptions;

public class InvalidDepartmentIdException extends RuntimeException{
    public InvalidDepartmentIdException() {
        super("Invalid Department Id");
    }
}
