package com.jxm.prod.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS对象存储相关配置
 * Created by macro on 2018/5/17.
 */
@Configuration
public class OssConfig {
    @Value("${aliYun.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;
    @Value("${aliYun.oss.accessKeyId}")
    private String ALIYUN_OSS_ACCESSKEYID;
    @Value("${aliYun.oss.accessKeySecret}")
    private String ALIYUN_OSS_ACCESSKEYSECRET;
    @Bean
    public OSSClient ossClient(){
        return new OSSClient(ALIYUN_OSS_ENDPOINT,ALIYUN_OSS_ACCESSKEYID,ALIYUN_OSS_ACCESSKEYSECRET);
    }
}
