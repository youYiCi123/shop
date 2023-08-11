package com.jxm.upstage.mapper;

import com.jxm.upstage.dao.SeriesModel;

import java.util.List;

public interface ProdSeriesMapper {
    List<SeriesModel> listAll();
    int create(SeriesModel seriesModel);
}
