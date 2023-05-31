package com.lojister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LojisterApplication {

    public static void main(String[] args) {

        SpringApplication.run(LojisterApplication.class, args);
    }

}