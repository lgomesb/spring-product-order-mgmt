package com.barbosa.ms.productordermgmt.product-order-mgmt.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardError {

    private final List<FieldMessage> messages = new ArrayList<>();
    
    
    public ValidationError(Integer status, String error, String messege, String path) {
        super(status, error, messege, path);
    }

    public void addError(String fieldName, String message) { 		
		messages.add(new FieldMessage(fieldName, message));
	}
    
    
}
