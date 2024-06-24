package com.jxm.file.mapper;

import com.jxm.file.dto.userIdUploadDto;
import com.jxm.file.entity.FileOperateLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "fileOperateLogMapper")
public interface FileOperateLogMapper {

    List<userIdUploadDto> getUserUploadCount();

    int insert(Long fileId,String fileName,Long userId,String operate,String extendField);

    void updateFileReason(Long fileId, String reason);

    void updateFilesReason(List<Long> fileIds, String reason);

    List<FileOperateLog> selectFileOperateLog(String startDate, String endDate,Long userId);

    List<FileOperateLog> getMyUploadFileStatus(String startDate, String endDate,Long userId,String keyword);

    int deleteLog(Long logId);

    int deleteBatchLog(List<Long> idList);
}
