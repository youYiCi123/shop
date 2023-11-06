package com.jxm.business.mapper;


import com.jxm.business.model.EmailConfig;

public interface EmailMapper {

    int save(EmailConfig emailConfig);

    int update(EmailConfig emailConfig);

    EmailConfig findById(Long id);

}
