package com.jxm.business.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class SmsConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionId;
    private String signName;
    private String templateCode;
    private String templateCodeTwo;
    private String endpoint;
}