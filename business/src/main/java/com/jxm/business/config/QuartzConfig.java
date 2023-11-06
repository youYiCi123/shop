package com.jxm.business.config;


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
        //每天的14点20分执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 20 14 * * ?");
        return TriggerBuilder.newTrigger().forJob(customTaskDetail())
                .withIdentity("CustomTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
    /*============================================================定时任务配置2============================================================*/
//    //指定具体的定时任务类
//    @Bean
//    public JobDetail uploadTaskDetail2() {
//        return JobBuilder.newJob(MyIncomeInformationTask2.class).withIdentity("MyHkInformationTask2").storeDurably().build();
//    }
//    //配置触发器
//    @Bean
//    public Trigger uploadTaskTrigger2() {
//        //TODO 这里设定执行方式
//        //每五秒执行一次
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
//        return TriggerBuilder.newTrigger().forJob(uploadTaskDetail2())
//                .withIdentity("MyHkInformationTask2")
//                .withSchedule(scheduleBuilder)
//                .build();
//    }
}
