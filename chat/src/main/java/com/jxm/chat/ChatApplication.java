package com.jxm.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jxm.chat","com.jxm.common"}) //Spring 默认扫描的是启动类所在的包，为了将 com.jxm.common 包中的@bean加入到扫描中，可以使用 @ComponentScan 指定额外的扫描包
@EnableFeignClients
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class,args);
    }
}
