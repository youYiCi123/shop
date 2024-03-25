package com.jxm.file.mapper;

import com.jxm.file.dto.userIdUploadDto;
import com.jxm.file.entity.FileOperateLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "fileOperateLogMapper")
public interface FileOperateLogMapper {

    List<userIdUploadDto> getUserUploadCount();

    int insert(Long fileId,Long userId,String operate,String extendField);

    List<FileOperateLog> selectFileOperateLog(String startDate, String endDate,Long userId);

    int deleteLog(Long logId);

    int deleteBatchLog(List<Long> idList);
}
