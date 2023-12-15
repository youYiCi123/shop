package com.jxm.business.mapper;

import com.jxm.business.dto.CalendarUserSendDto;
import com.jxm.business.model.CalendarParam;

import java.util.List;

public interface CalendarMapper {

    int add(CalendarParam calendarParam);

    int update(CalendarParam calendarParam);

    int delete(Long id);

    List<CalendarParam> queryContent(Long id);

    List<CalendarUserSendDto> getCalendarJobByToday();
}
