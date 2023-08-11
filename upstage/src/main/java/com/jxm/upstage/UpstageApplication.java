package com.jxm.upstage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.jxm.upstage.mapper")  //使用前请导入mybatis-spring-boot-starter依赖包
@EnableFeignClients
public class UpstageApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpstageApplication.class,args);
    }
}
