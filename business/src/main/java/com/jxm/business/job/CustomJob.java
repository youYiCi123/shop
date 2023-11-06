package com.jxm.business.job;

import com.jxm.business.dto.EmailVo;
import com.jxm.business.model.CustomParam;
import com.jxm.business.service.CustomService;
import com.jxm.business.service.EmailService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomJob extends QuartzJobBean {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomService customService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        List<CustomParam> customByNearDeadlines = customService.getCustomByNearDeadline();
        customByNearDeadlines.stream().forEach(t->{
            EmailVo emailVo = new EmailVo();
            emailVo.setTos(t.getSalesPersonEmail());
            emailVo.setSubject("客户证件到期提醒");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            emailVo.setContent("你所负责的客户（" + t.getCustomName() + "）的许可证到期时间为" + sdf.format(t.getLicenseTime()));
            //后期使用消息中间件
            emailService.send(emailVo,emailService.find());
        });
    }
}
