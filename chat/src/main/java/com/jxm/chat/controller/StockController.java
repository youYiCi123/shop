package com.jxm.chat.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @GetMapping("/getStock")
    @SentinelResource(value = "getStock",blockHandler = "flowBlockHandler")
    public String getStock(){
        System.out.println("获得仓库");
        return "获得仓库";
    }

    //流控规则
    public String flowBlockHandler(BlockException e){
        return "留空";
    }
}
