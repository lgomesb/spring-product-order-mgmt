package com.barbosa.ms.invetorymgmt.productorder.exception;

public class InvalidProductOrderStatusException extends RuntimeException {

    public InvalidProductOrderStatusException(String message) {
        super(message);
    }
}
