package com.jxm.prod.service.Impl;

import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.prod.dao.SeriesModel;
import com.jxm.prod.feign.UserFeignService;
import com.jxm.prod.mapper.ProdMapper;
import com.jxm.prod.service.ProdService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdServiceImpl implements ProdService {

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private UserFeignService userFeignService;

    @GlobalTransactional
    @Override
    public int create(SeriesModel seriesModel) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        seriesModel.setId(idGenerator.nextId());
        userFeignService.create(seriesModel);
        int i= prodMapper.create(seriesModel);
        int p=10/0;
        return i;
    }

}
