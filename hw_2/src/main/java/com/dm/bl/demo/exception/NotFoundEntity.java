package com.dm.bl.demo.exception;

public class NotFoundEntity extends RuntimeException{
    public NotFoundEntity(String message) {
        super(message);
    }
}
