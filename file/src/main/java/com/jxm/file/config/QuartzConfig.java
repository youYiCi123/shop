package com.jxm.file.config;

import com.jxm.file.job.FileJob;
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
    public JobDetail fileTaskDetail() {
        return JobBuilder.newJob(FileJob.class).withIdentity("FileTask").storeDurably().build();
    }
    //配置触发器
    @Bean
    public Trigger uploadTaskTrigger() {
        //每天的上午9点30分执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 04 15 * * ?");
        return TriggerBuilder.newTrigger().forJob(fileTaskDetail())
                .withIdentity("FileTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
