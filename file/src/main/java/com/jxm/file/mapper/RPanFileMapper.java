package com.jxm.file.mapper;

import com.jxm.file.entity.RPanFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物理文件数据层
 */
@Repository(value = "rPanFileMapper")
public interface RPanFileMapper {

    int deleteByPrimaryKey(Long fileId);

    //批量删除
    int deleteByPrimaryKeys(List<Long> fileIds);

    int insert(RPanFile record);

    int insertSelective(RPanFile record);

    RPanFile selectByPrimaryKey(Long fileId);

    int updateByPrimaryKeySelective(RPanFile record);

    int updateByPrimaryKey(RPanFile record);

    List<RPanFile> selectByFileIdList(@Param("fileIdList") List<Long> fileIdList);

    int deleteBatch(@Param("fileIdList") List<String> fileIdList);

    List<RPanFile> selectByIdentifier(@Param("identifier") String identifier);

    List<RPanFile> selectRPanFileList(@Param("batchIndex") Long batchIndex, @Param("batchSize") Long batchSize);

    String filePathById(Long fileId);

    List<String> filePathByIds(@Param("realFileIds") List<Long> realFileIds);

}