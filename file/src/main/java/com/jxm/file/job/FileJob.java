package com.jxm.file.job;

import com.jxm.file.service.IUserFileService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FileJob extends QuartzJobBean {

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        iUserFileService.getALLExpireFile();
    }
}
