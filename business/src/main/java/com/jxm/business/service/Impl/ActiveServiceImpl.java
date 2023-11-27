package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.jxm.business.dto.ActiveSubmitDto;
import com.jxm.business.dto.TempValueSubmitSingerDto;
import com.jxm.business.dto.UserDepDto;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.ActiveUserMapper;
import com.jxm.business.mapper.QuUserMapper;
import com.jxm.business.mapper.SurveyUserMapper;
import com.jxm.business.model.ActiveUserParam;
import com.jxm.business.model.QuUserParam;
import com.jxm.business.service.ActiveService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActiveServiceImpl implements ActiveService {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private ActiveUserMapper activeUserMapper;

    @Autowired
    private QuUserMapper quUserMapper;

    @Override
    public List<ActiveUserParam> getActiveBySearch(String startDate, String endDate, String keyword) {
        return activeUserMapper.getActiveBySearch(startDate,endDate,keyword);
    }

    @Override
    public int submitContent(ActiveSubmitDto activeSubmitDto) throws ParseException {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1, 1);

        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);

        ActiveUserParam activeUserParam = new ActiveUserParam();
        //活动日期
        if (activeSubmitDto.getActivityTime().length == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = sdf.parse(activeSubmitDto.getActivityTime()[0]);
            Date endDate = sdf.parse(activeSubmitDto.getActivityTime()[1]);
            activeUserParam.setStartTime(startDate);
            activeUserParam.setEndTime(endDate);
        }else {
            activeUserParam.setStartTime(null);
            activeUserParam.setEndTime(null);
        }
        //参与部门
        String[] depIds = activeSubmitDto.getDepIds();
        if (depIds != null) {
            String resultString = "";
            for (int i = 0; i < depIds.length; i++) {
                if (i < depIds.length - 1) {
                    resultString += depIds[i] + ",";
                } else {
                    resultString += depIds[i];
                }
            }
            activeUserParam.setDepIds(resultString);
        } else {
            activeUserParam.setDepIds(null);
        }
        long nextId = idGenerator.nextId();
        activeUserParam.setId(nextId);
        activeUserParam.setTempId(activeSubmitDto.getTempId());
        activeUserParam.setTempName(activeSubmitDto.getTempName());
        activeUserParam.setActiveName(activeSubmitDto.getName());
        activeUserParam.setActiveAddress(activeSubmitDto.getAddress());
        activeUserParam.setHandlerUserId(activeSubmitDto.getHandlerUserId());
        activeUserParam.setUserId(userDepDto.getUserId());
        activeUserParam.setUserName(userDepDto.getNickName());
        activeUserParam.setCreateTime(new Date());
        activeUserMapper.add(activeUserParam);

        //填写问卷信息
        List<QuUserParam> quUserParams = new ArrayList<>();
        for (TempValueSubmitSingerDto tempValueSubmitDto : activeSubmitDto.getTempValueSubmitSingerDtos()) {
            QuUserParam surveyParam = new QuUserParam();
            BeanUtils.copyProperties(tempValueSubmitDto, surveyParam);
            String[] checkValue = tempValueSubmitDto.getCheckValue();

            if (checkValue != null) {
                String resultString = "";
                for (int i = 0; i < checkValue.length; i++) {
                    if (i < checkValue.length - 1) {
                        resultString += checkValue[i] + ",";
                    } else {
                        resultString += checkValue[i];
                    }
                }
                surveyParam.setCheckValue(resultString);
            } else {
                surveyParam.setCheckValue(null);
            }
            surveyParam.setId(idGenerator.nextId());
            surveyParam.setUserId(userDepDto.getUserId());
            surveyParam.setRelateId(nextId);
            surveyParam.setCreateTime(new Date());
            quUserParams.add(surveyParam);
        }
        return quUserMapper.saveBatch(quUserParams);
    }

    @Override
    public int delete(Long id) {
        activeUserMapper.deleteById(id);
        return quUserMapper.deleteByRelateId(id);
    }
}
