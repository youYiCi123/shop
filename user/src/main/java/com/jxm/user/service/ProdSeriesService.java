package com.jxm.user.service;

import com.jxm.user.dao.SeriesModel;

import java.util.List;

public interface ProdSeriesService {
    List<SeriesModel> listAll();
    int create(SeriesModel seriesModel);
}
