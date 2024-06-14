package com.barbosa.ms.invetorymgmt.productorder.exception;

public class ProductOrderAlreadyCompletedException extends RuntimeException {

    public ProductOrderAlreadyCompletedException(String message) {
        super(message);
    }
}
