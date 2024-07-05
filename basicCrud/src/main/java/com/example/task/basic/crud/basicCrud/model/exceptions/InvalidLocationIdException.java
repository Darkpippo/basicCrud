package com.example.task.basic.crud.basicCrud.model.exceptions;

public class InvalidLocationIdException extends RuntimeException{
    public InvalidLocationIdException() {
        super("Invalid location Id.");
    }
}