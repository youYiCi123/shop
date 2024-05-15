package com.jxm.business.config;


import com.jxm.business.job.CalendarJob;
import com.jxm.business.job.CertificateJob;
import com.jxm.business.job.CustomJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz定时任务配置类
 * @author pan_junbiao
 **/
@Configuration
public class QuartzConfig {
    /*============================================================定时任务配置1============================================================*/
    //指定具体的定时任务类
    @Bean
    public JobDetail customTaskDetail() {
        return JobBuilder.newJob(CustomJob.class).withIdentity("CustomTask").storeDurably().build();
    }
    //配置触发器
    @Bean
    public Trigger uploadTaskTrigger() {
        //每天的上午9点30分执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 30 9 * * ?");
        return TriggerBuilder.newTrigger().forJob(customTaskDetail())
                .withIdentity("CustomTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
    /*============================================================定时任务配置2============================================================*/
    //指定具体的定时任务类
    @Bean
    public JobDetail uploadTaskDetail2() {
        return JobBuilder.newJob(CalendarJob.class).withIdentity("CalendarTask").storeDurably().build();
    }
    //配置触发器
    @Bean
    public Trigger uploadTaskTrigger2() {
        //每天的上午9点30分执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 39 11 * * ? ");
        return TriggerBuilder.newTrigger().forJob(uploadTaskDetail2())
                .withIdentity("CalendarTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
    /*--------------------------定时任务配置3------------------------------*/
    //指定具体的定时任务类
    @Bean
    public JobDetail uploadTaskDetail3() {
        return JobBuilder.newJob(CertificateJob.class).withIdentity("CertificateTask").storeDurably().build();
    }
    //配置触发器
    @Bean
    public Trigger uploadTaskTrigger3() {
        //每天的上午9点30分执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 07 16 * * ? ");
        return TriggerBuilder.newTrigger().forJob(uploadTaskDetail3())
                .withIdentity("CertificateTask")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
