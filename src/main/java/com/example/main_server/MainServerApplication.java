package com.example.main_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:properties/env.properties")
@SpringBootApplication
public class MainServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServerApplication.class, args);
    }

}
