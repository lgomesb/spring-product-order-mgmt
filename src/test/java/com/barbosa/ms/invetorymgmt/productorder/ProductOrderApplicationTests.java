package com.barbosa.ms.invetorymgmt.productorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles(value = "test")
public class ProductOrderApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(ProductOrderApplicationTests.class, args);
	}

}
