package com.barbosa.ms.productordermgmt.product-order-mgmt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductOrderMgmtApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProductOrderMgmtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("ProductOrderMgmt");
    }
}