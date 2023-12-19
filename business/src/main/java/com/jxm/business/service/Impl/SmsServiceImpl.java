package com.jxm.business.service.Impl;

import ch.qos.logback.core.net.server.Client;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jxm.business.config.SmsConfig;
import com.jxm.business.service.SmsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsConfig smsConfig;

    private static final String REGION_ID = "cn-hangzhou";
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    @Override
    public Boolean send(String phone, String customName, String licenseTime, String residueTime) {
        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID,smsConfig.getAccessKeyId(),smsConfig.getAccessKeySecret());

            DefaultProfile.addEndpoint(REGION_ID, REGION_ID, PRODUCT, DOMAIN);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();

            request.setMethod(MethodType.POST);

            // 手机号可以单个也可以多个（多个用逗号隔开，如：15*******13,13*******27,17*******56）
            request.setPhoneNumbers(phone);

            request.setSignName(smsConfig.getSignName());

            request.setTemplateCode(smsConfig.getTemplateCode());

            /*  例如签名内容为：tianYiMsg
             *你所负责的客户${customName},许可证到期时间为${time},即将在90天后过期,请及时联系该客户
             */
            request.setTemplateParam("{\"customName\":\"" + customName + "\"," +
                    "\"time\":\"" + licenseTime + "\"," +
                    "\"residueTime\":\"" + residueTime + "\"}");

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if ((sendSmsResponse.getCode() != null) && (sendSmsResponse.getCode().equals("OK"))) {
                log.info("发送成功,code:" + sendSmsResponse.getCode());
                return true;
            } else {
                log.info("发送失败,code:" + sendSmsResponse.getCode());
                return false;
            }
        } catch (ClientException e) {
            log.error("发送失败,系统错误！", e);
            return false;
        }
    }

    @Override
    public Boolean sendCalendar(String phone, String title, String endTime) {
        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID,smsConfig.getAccessKeyId(),smsConfig.getAccessKeySecret());

            DefaultProfile.addEndpoint(REGION_ID, REGION_ID, PRODUCT, DOMAIN);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();

            request.setMethod(MethodType.POST);

            // 手机号可以单个也可以多个（多个用逗号隔开，如：15*******13,13*******27,17*******56）
            request.setPhoneNumbers(phone);

            request.setSignName(smsConfig.getSignName());

            request.setTemplateCode(smsConfig.getTemplateCodeTwo());

            /*  例如签名内容为：tianYiMsg
             *你设定的的日程（${title}）已开始，请于${endTime}前完成"
             */
            request.setTemplateParam("{\"title\":\"" + title + "\"," +
                    "\"endTime\":\"" + endTime +"\"}");

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if ((sendSmsResponse.getCode() != null) && (sendSmsResponse.getCode().equals("OK"))) {
                log.info("发送成功,code:" + sendSmsResponse.getCode());
                return true;
            } else {
                log.info("发送失败,code:" + sendSmsResponse.getCode());
                return false;
            }
        } catch (ClientException e) {
            log.error("发送失败,系统错误！", e);
            return false;
        }
    }
}
