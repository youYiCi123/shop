package com.jxm.file.service;

import com.jxm.common.api.CommonPage;
import com.jxm.file.dto.FileOperateLogDetail;
import com.jxm.file.dto.MyUploadFileStatus;
import com.jxm.file.dto.UserUploadCountDto;

import java.util.List;

public interface FileOperateService {

    List<UserUploadCountDto> getUserUploadCount();

    CommonPage<FileOperateLogDetail> getFileOperateLog(String startDate, String endDate, Long userId, Integer pageNum, Integer pageSize);

    CommonPage<MyUploadFileStatus> getMyUploadFileStatus(String startDate, String endDate, Long userId,String keyword, Integer pageNum, Integer pageSize);

    int deleteLog(Long logId);

    int deleteBatchLog(List<Long> idList);
}
