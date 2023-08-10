package com.jxm.prod.feign;

import com.jxm.common.api.CommonResult;
import com.jxm.prod.dao.SeriesModel;
import org.springframework.stereotype.Component;

@Component
public class UserFeignServiceFallBack implements UserFeignService {
    @Override
    public CommonResult create(SeriesModel seriesModel) {
        return  CommonResult.success("降级了");
    }
}
