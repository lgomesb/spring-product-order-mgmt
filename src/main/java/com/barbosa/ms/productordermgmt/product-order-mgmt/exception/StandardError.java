package com.barbosa.ms.productordermgmt.product-order-mgmt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class StandardError implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp; 
	private Integer status;
	private String error; 

	@JsonInclude(Include.NON_NULL)
	private String messege; 
	private String path;
	
	@Builder
	public StandardError(Integer status, String error, String messege, String path) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.status = status;
		this.error = error;
		this.messege = messege;
		this.path = path;
	}
	
}