package com.example.task.basic.crud.basicCrud.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidLocationIdException extends RuntimeException{
    public InvalidLocationIdException() {
        super("No location with the given id exists.");
    }
}