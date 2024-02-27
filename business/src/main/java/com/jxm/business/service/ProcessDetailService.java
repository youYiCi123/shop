package com.jxm.business.service;

import com.jxm.business.dto.ProcessDetailDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProcessDetailService {

    ProcessDetailDto queryDetailContent(Long id);

    int updateDetailContent(ProcessDetailDto processDetailDto);
}
