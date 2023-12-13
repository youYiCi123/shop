package com.jxm.file.service;

import com.jxm.file.dto.FileOperateLogDetail;
import com.jxm.file.dto.UserUploadCountDto;

import java.util.List;

public interface FileOperateService {

    List<UserUploadCountDto> getUserUploadCount();

    List<FileOperateLogDetail> getFileOperateLog(String startDate, String endDate, Long userId, Integer pageNum, Integer pageSize);

    int deleteLog(Long logId);

    int deleteBatchLog(List<Long> idList);
}
