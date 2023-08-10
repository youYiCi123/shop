package com.jxm.user.mapper;

import com.jxm.user.dao.SeriesModel;

import java.util.List;

public interface ProdSeriesMapper {
    List<SeriesModel> listAll();
    int create(SeriesModel seriesModel);
}
