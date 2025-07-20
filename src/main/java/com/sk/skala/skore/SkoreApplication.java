package com.sk.skala.skore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource(
        value = "classpath:properties/env.properties",
        ignoreResourceNotFound = true
)
@SpringBootApplication
public class SkoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkoreApplication.class, args);
    }

}
