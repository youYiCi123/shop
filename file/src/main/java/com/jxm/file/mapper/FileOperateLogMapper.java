package com.jxm.file.mapper;

import com.jxm.file.entity.FileOperateLog;
import com.jxm.file.vo.RPanUserFileDisplayVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "fileOperateLogMapper")
public interface FileOperateLogMapper {

    int insert(Long fileId,Long userId,String operate);

    List<FileOperateLog> selectFileOperateLog(String startDate, String endDate,Long userId);

    int deleteLog(Long logId);

    int deleteBatchLog(List<Long> idList);
}
