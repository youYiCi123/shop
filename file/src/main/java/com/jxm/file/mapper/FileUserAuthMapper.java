package com.jxm.file.mapper;

import com.jxm.file.entity.FileUserAuth;
import org.apache.ibatis.annotations.Param;

public interface FileUserAuthMapper {
    FileUserAuth select(@Param("fileId") Long fileId,@Param("userId") Long userId);
    int insert(FileUserAuth userFileAuth);
    int update(FileUserAuth userFileAuth);
}
