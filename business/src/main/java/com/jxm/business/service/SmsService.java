package com.jxm.business.service;

public interface SmsService {

    Boolean send(String phone, String customName,String licenseTime,String residueTime);

    Boolean sendCalendar(String phone, String title,String endTime);
}
