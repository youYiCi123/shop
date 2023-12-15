package com.jxm.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CalendarUserSendDto {
    private String title;//日程内容
    private Date startDate;//日程起始时间
    private Date endDate;//日程截至时间
    private String userName;
    private String userPhone;
    private String userEmail;
}
