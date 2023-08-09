package com.jxm.prod.controller;

import com.jxm.prod.feign.UserFeignService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private UserFeignService userFeignService;

    @ApiOperation("王瑞12获取了token")
    @GetMapping("/getProd")
   public String getProd(){
       System.out.println("下单成功");
        String userName=userFeignService.getUser2();
       return "Hello word12wr"+userName;
   }
}
