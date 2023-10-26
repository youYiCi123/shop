package com.jxm.business.mapper;


import com.jxm.business.model.NewsPostParam;
import com.jxm.business.model.NewsShowHomeParam;

import java.util.List;

public interface NewsMapper {

    List<NewsPostParam> getNewsByDateAndKeyword(String startDate, String endDate, String keyword, Long newsType);

    List<NewsShowHomeParam> getNewsToHome();

    int setViewCount(Long id);

    int delete(Long newsId);

    int add(NewsPostParam newsPostParam);

    int update(NewsPostParam newsPostParam);

    int deleteBatchTrain(List<Long> idList);

}
