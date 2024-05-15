package com.jxm.business.job;

import com.jxm.business.dto.CalendarUserSendDto;
import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.dto.EmailVo;
import com.jxm.business.model.CertificateRemindParam;
import com.jxm.business.service.CalendarService;
import com.jxm.business.service.CertificateService;
import com.jxm.business.service.EmailService;
import com.jxm.business.service.SmsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class CertificateJob extends QuartzJobBean {
    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CertificateService certificateService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        CertificateRemindParam remind = certificateService.getRemind();
        List<CertificateBriefDto> certificateBriefDtos = certificateService.getCertificateByNearDeadline(remind.getForwardDays());
        certificateBriefDtos.stream().forEach(t->{
            EmailVo emailVo = new EmailVo();
            emailVo.setTos(remind.getRemindPersonEmail());
            emailVo.setSubject("证书临期提醒");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            emailVo.setContent("<<" + t.getCertificateName()+ ">>的下次申报日期即将临期，请在" + sdf.format(t.getLastDeclareTime())+"前完成");
            //后期使用消息中间件
            emailService.send(emailVo,emailService.find());
            //smsService.sendCalendar(t.getUserPhone(),t.getTitle(),sdf.format(t.getEndDate()));
        });
    }
}
