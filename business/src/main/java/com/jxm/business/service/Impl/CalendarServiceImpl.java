package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.jxm.business.dto.*;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.CalendarMapper;
import com.jxm.business.mapper.CalendarUserMapper;
import com.jxm.business.model.CalendarParam;
import com.jxm.business.model.CalendarUserParam;
import com.jxm.business.service.CalendarService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private CalendarUserMapper calendarUserMapper;

    @Override
    @Transactional
    public int add(CalendarUserDto calendarUserDto) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1, 1);
        long calendarId = idGenerator.nextId();
        CalendarParam calendarParam = new CalendarParam();
        CalendarUserParam calendarUserParam = new CalendarUserParam();
        calendarParam.setId(calendarId);
        calendarParam.setTitle(calendarUserDto.getTitle());
        calendarParam.setStartDate(calendarUserDto.getStartDate());
        calendarParam.setEndDate(calendarUserDto.getEndDate());
        calendarParam.setLevel(calendarUserDto.getLevel());
        calendarParam.setIsRemind(calendarUserDto.getIsRemind());
        calendarParam.setCreateDate(new Date());
        calendarMapper.add(calendarParam);
        calendarUserParam.setId(idGenerator.nextId());
        calendarUserParam.setCalendarId(calendarId);
        UmsAdminConcat umsAdminConcat = upstageService.getUmsAdminConcat(calendarUserDto.getUserId()).getData();
        calendarUserParam.setUserId(calendarUserDto.getUserId());
        calendarUserParam.setUserName(umsAdminConcat.getNickName());
        calendarUserParam.setUserEmail(umsAdminConcat.getEmail());
        calendarUserParam.setUserPhone(umsAdminConcat.getPhone());
        return calendarUserMapper.add(calendarUserParam);
    }

    @Override
    public int update(CalendarUserDto calendarUserDto) {
        CalendarParam calendarParam = new CalendarParam();
        calendarParam.setId(calendarUserDto.getId());
        calendarParam.setTitle(calendarUserDto.getTitle());
        calendarParam.setStartDate(calendarUserDto.getStartDate());
        calendarParam.setEndDate(calendarUserDto.getEndDate());
        calendarParam.setLevel(calendarUserDto.getLevel());
        calendarParam.setIsRemind(calendarUserDto.getIsRemind());
        return calendarMapper.update(calendarParam);
    }

    @Override
    public int delete(Long id) {
        calendarMapper.delete(id);
        return calendarUserMapper.deleteByCalendarId(id);
    }

    @Override
    public List<CalendarDto> queryContent(Long id) {
        List<CalendarDto> calendarDtos = new ArrayList<>();
        List<CalendarParam> calendarParams = calendarMapper.queryContent(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!CollectionUtils.isEmpty(calendarParams)) {
            for (CalendarParam calendarParam : calendarParams) {
                CalendarDto calendarDto = new CalendarDto();
                calendarDto.setId(calendarParam.getId());
                calendarDto.setTitle(calendarParam.getTitle());
                calendarDto.setStart(sdf.format(calendarParam.getStartDate()));
                calendarDto.setEnd(sdf.format(calendarParam.getEndDate()));
                calendarDto.setClassName(calendarParam.getLevel());
                calendarDtos.add(calendarDto);
            }
            return calendarDtos;
        }
        return Arrays.asList();
    }

    @Override
    public List<CalendarUserSendDto> getCalendarJobByToday() {
       return calendarMapper.getCalendarJobByToday();
    }
}
