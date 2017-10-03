package com.example.demo.springboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author mykola
 */
@SpringBootApplication
@EnableJpaRepositories
public class SpringBootDemoApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApp.class, args);
    }
}
