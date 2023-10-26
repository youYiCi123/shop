package com.jxm.business.service;


import com.jxm.business.model.NewsHomeDetail;
import com.jxm.business.model.NewsParam;
import com.jxm.business.model.NewsPostParam;
import com.jxm.business.model.NewsShowHomeParam;

import java.text.ParseException;
import java.util.List;

public interface NewsService {

    List<NewsPostParam> getNewsByDateAndKeyword(String startDate, String endDate, String keyword, Long newsType);

    List<NewsShowHomeParam> getNewsToHome();

    int delete(Long newsId);

    int deleteBatchNews(List<Long> idList);

    NewsParam queryNewsContent(Long id);

    NewsHomeDetail queryNewsPreContent(Long id);

    int addContent(NewsParam newsParam) throws ParseException;

    int updateContent(NewsParam newsContentParam);

}
