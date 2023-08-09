package com.jxm.user.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RefreshScope  //@Value无法动态感知配置文件内容修改
public class UserController {

    @Value("${user.name}")
    private String name;

    @GetMapping("/getUser")
    public String getUser() throws InterruptedException {
        System.out.println("用户成功"+name);
        return "Hello word11"+name;
    }

    @ApiOperation("王瑞获取了token")
    @GetMapping("/getUser2")
    public String getUser2(){
        System.out.println("用户成功"+name);
        return "Hello word11"+name;
    }
}
