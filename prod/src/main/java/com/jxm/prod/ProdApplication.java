package com.jxm.prod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProdApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProdApplication.class,args);
    }
}
