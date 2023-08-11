package com.jxm.upstage.service.Impl;

import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.upstage.dao.SeriesModel;
import com.jxm.upstage.mapper.ProdSeriesMapper;
import com.jxm.upstage.service.ProdSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdSeriesServiceImpl implements ProdSeriesService {

    @Autowired
    private ProdSeriesMapper prodSeriesMapper;

    @Override
    public List<SeriesModel> listAll() {

        return prodSeriesMapper.listAll();
    }

    @Override
    public int create(SeriesModel seriesModel) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        seriesModel.setId(idGenerator.nextId());
        return prodSeriesMapper.create(seriesModel);
    }

}
