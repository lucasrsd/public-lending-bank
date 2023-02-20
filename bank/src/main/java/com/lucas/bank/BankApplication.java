package com.lucas.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(BankApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
