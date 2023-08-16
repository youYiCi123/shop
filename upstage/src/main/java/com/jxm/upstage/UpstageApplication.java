package com.jxm.upstage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jxm.upstage","com.jxm.common"}) //Spring 默认扫描的是启动类所在的包，为了将 com.jxm.common 包中的@bean加入到扫描中，可以使用 @ComponentScan 指定额外的扫描包
//指定 @ComponetScan 后默认的扫描位置会发生改变，因此添加新的扫描包后要确保启动类所在的包也能被扫描到！
@MapperScan("com.jxm.upstage.mapper")  //使用前请导入mybatis-spring-boot-starter依赖包
@EnableFeignClients
public class UpstageApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpstageApplication.class,args);
    }
}
