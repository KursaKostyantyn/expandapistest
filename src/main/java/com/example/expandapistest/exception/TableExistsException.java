package com.example.expandapistest.exception;

public class TableExistsException extends RuntimeException{
    public TableExistsException(String message){
        super(message);
    }
}
