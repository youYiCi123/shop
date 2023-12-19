package com.jxm.business.job;

import com.jxm.business.dto.CalendarUserSendDto;
import com.jxm.business.dto.EmailVo;
import com.jxm.business.model.CustomParam;
import com.jxm.business.service.CalendarService;
import com.jxm.business.service.CustomService;
import com.jxm.business.service.EmailService;
import com.jxm.business.service.SmsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarJob extends QuartzJobBean {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CalendarService calendarService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<CalendarUserSendDto> customByNearDeadlines = calendarService.getCalendarJobByToday();
        customByNearDeadlines.stream().forEach(t->{
            EmailVo emailVo = new EmailVo();
            emailVo.setTos(t.getUserEmail());
            emailVo.setSubject("任务开始提醒");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            emailVo.setContent("你设定的的日程（" + t.getTitle() + "）已开始，请在" + sdf.format(t.getEndDate())+"前完成");
            //后期使用消息中间件
            emailService.send(emailVo,emailService.find());
            smsService.sendCalendar(t.getUserPhone(),t.getTitle(),sdf.format(t.getEndDate()));
        });
    }
}
