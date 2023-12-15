package com.jxm.business.mapper;

import com.jxm.business.model.CalendarUserParam;

public interface CalendarUserMapper {

    int add(CalendarUserParam calendarUserParam);

    int deleteByCalendarId(Long id);
}
