package com.jxm.business.job;

import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.dto.EmailVo;
import com.jxm.business.dto.SupplierBriefDto;
import com.jxm.business.model.RemindParam;
import com.jxm.business.service.EmailService;
import com.jxm.business.service.SupplierService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class SupplierJob extends QuartzJobBean {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SupplierService supplierService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RemindParam remind = supplierService.getRemind();
        List<SupplierBriefDto> supplierBriefDtos = supplierService.getSupplierByNearDeadline(remind.getForwardDays());
        supplierBriefDtos.stream().forEach(t->{
            EmailVo emailVo = new EmailVo();
            emailVo.setTos(remind.getRemindPersonEmail());
            emailVo.setSubject("供应商临期提醒");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            emailVo.setContent("你所负责的供应商" + t.getSupplierName()+ "的经营授权有效期即将临期，请在" + sdf.format(t.getBusinessAuthTime())+"前完成");
            //后期使用消息中间件
            emailService.send(emailVo,emailService.find());
            //smsService.sendCalendar(t.getUserPhone(),t.getTitle(),sdf.format(t.getEndDate()));
        });
    }
}
