package com.jxm.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jxm.user.mapper")  //使用前请导入mybatis-spring-boot-starter依赖包
public class UserApplication{
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
