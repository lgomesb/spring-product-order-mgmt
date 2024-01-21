package com.barbosa.ms.productordermgmt.productordermgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles(value = "test")
public class ProductOrderMgmtApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(ProductOrderMgmtApplicationTests.class, args);
	}

}
