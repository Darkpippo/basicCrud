package com.example.task.basic.crud.basicCrud.model.exceptions;

public class InvalidLocationException extends RuntimeException {
    public InvalidLocationException() {
        super("Invalid location parameters");
    }
}
