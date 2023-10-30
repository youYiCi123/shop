package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.jxm.business.dto.UserDepDto;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.NewsContentMapper;
import com.jxm.business.mapper.NewsMapper;
import com.jxm.business.model.NewsHomeDetail;
import com.jxm.business.model.NewsParam;
import com.jxm.business.model.NewsPostParam;
import com.jxm.business.model.NewsShowHomeParam;
import com.jxm.business.service.NewsService;
import com.jxm.common.api.CommonResult;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsContentMapper newsContentMapper;

    @Autowired
    private UpstageService upstageService;
    @Override
    public List<NewsPostParam> getNewsByDateAndKeyword(String startDate, String endDate, String keyword, Long newsType) {

        return newsMapper.getNewsByDateAndKeyword(startDate,  endDate,  keyword,newsType);
    }

    @Override
    public List<NewsShowHomeParam> getNewsToHome() {
        return newsMapper.getNewsToHome();
    }

    @Override
    public int delete(Long trainId) {
        int count = newsMapper.delete(trainId);
        if (count > 0) {
            return newsContentMapper.delete(trainId);
        }
        return -1;
    }

    @Override
    public int deleteBatchNews(List<Long> idList) {
        int count = newsMapper.deleteBatchTrain(idList);
        if (count > 0) {
            return newsContentMapper.deleteBatchNews(idList);
        }
        return -1;
    }

    @Override
    public NewsParam queryNewsContent(Long id) {
        return newsContentMapper.queryNewsContent(id);
    }

    @Override
    public NewsHomeDetail getNewsTopToHome() {
        return newsContentMapper.getNewsTopToHome();
    }

    @Override
    public NewsHomeDetail queryNewsPreContent(Long id) {
        NewsHomeDetail newsPreDetail = newsContentMapper.queryNewsPreContent(id);
        if(newsPreDetail!=null){
            newsMapper.setViewCount(id);
        }
        return newsPreDetail;
    }

    @Override
    public int addContent(NewsParam newsParam) throws ParseException {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        long id = idGenerator.nextId();
        newsParam.setId(id);
        int count= newsContentMapper.addContent(newsParam);
        if(count>0){
            NewsPostParam newsPostParam=new NewsPostParam();
            newsPostParam.setId(id);
            newsPostParam.setTheme(newsParam.getTheme());
            newsPostParam.setCreatedTime(new Date());
            newsPostParam.setNewsType(newsParam.getNewsType());
            String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
            UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
            newsPostParam.setCreatedAdminId(userDepDto.getUserId());
            newsPostParam.setCreatedAdminName(userDepDto.getNickName());
            return newsMapper.add(newsPostParam);
        }
        return 0;
    }

    @Override
    public int updateContent(NewsParam newsContentParam) {
        int count=newsContentMapper.updateContent(newsContentParam);
        if(count>0){
            NewsPostParam newsPostParam=new NewsPostParam();
            newsPostParam.setId(newsContentParam.getId());
            newsPostParam.setNewsType(newsContentParam.getNewsType());
            newsPostParam.setTheme(newsContentParam.getTheme());
            return newsMapper.update(newsPostParam);
        }
        return 0;
    }
}
