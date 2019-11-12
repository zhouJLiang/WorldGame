package com.example.objecteureka.objecteuerkapeer2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
//@EnableDiscoveryClient
public class Objecteuerkapeer2Application {

    public static void main(String[] args) {
        SpringApplication.run(Objecteuerkapeer2Application.class, args);
    }

}
