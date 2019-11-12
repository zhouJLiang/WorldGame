package com.example.eureka.objecteureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
//@EnableEurekaClient
public class ObjecteurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObjecteurekaApplication.class, args);
    }

}
