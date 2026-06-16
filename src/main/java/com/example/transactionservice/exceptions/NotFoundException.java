package com.example.transactionservice.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String ex) {
        super(ex);
    }
}
