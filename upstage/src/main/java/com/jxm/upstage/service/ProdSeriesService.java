package com.jxm.upstage.service;

import com.jxm.upstage.dao.SeriesModel;

import java.util.List;

public interface ProdSeriesService {
    List<SeriesModel> listAll();
    int create(SeriesModel seriesModel);
}
