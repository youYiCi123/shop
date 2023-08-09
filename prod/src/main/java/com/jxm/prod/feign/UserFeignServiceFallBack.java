package com.jxm.prod.feign;

import org.springframework.stereotype.Component;

@Component
public class UserFeignServiceFallBack implements UserFeignService {
    @Override
    public String getUser() {
        return "getUser降级了";
    }

    @Override
    public String getUser2() {
        return "getUser2降级了";
    }
}
