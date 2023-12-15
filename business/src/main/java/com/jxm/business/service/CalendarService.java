package com.jxm.business.service;

import com.jxm.business.dto.CalendarDto;
import com.jxm.business.dto.CalendarUserDto;
import com.jxm.business.dto.CalendarUserSendDto;
import com.jxm.business.model.CalendarParam;
import com.jxm.business.model.CustomParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.util.List;

public interface CalendarService {

    int add(CalendarUserDto calendarUserDto) throws ParseException;

    int update(CalendarUserDto calendarUserDto);

    int delete(Long id);

    List<CalendarDto> queryContent(Long id);

    List<CalendarUserSendDto> getCalendarJobByToday();
}
