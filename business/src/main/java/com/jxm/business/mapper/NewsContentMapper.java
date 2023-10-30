package com.jxm.business.mapper;


import com.jxm.business.model.NewsHomeDetail;
import com.jxm.business.model.NewsParam;
import com.jxm.business.model.NewsPostParam;

import java.util.List;

public interface NewsContentMapper {

    NewsParam queryNewsContent(Long id);

    NewsHomeDetail getNewsTopToHome();

    NewsHomeDetail queryNewsPreContent(Long id);

    int updateContent(NewsParam trainContentParam);

    int addContent(NewsParam trainContentParam);

    int delete(Long newsId);

    int deleteBatchNews(List<Long> idList);
}
