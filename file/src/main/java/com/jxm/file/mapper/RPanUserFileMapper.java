package com.jxm.file.mapper;

import com.jxm.file.bo.FilePositionBO;
import com.jxm.file.dto.DashboardUserFileParam;
import com.jxm.file.entity.RPanUserFile;
import com.jxm.file.vo.RPanUserFileDisplayVO;
import com.jxm.file.vo.RPanUserFileSearchVO;
import com.jxm.file.vo.RPanUserFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户文件数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanUserFileMapper")
public interface RPanUserFileMapper {

    List<DashboardUserFileParam> getTheNumberOfFileTypes(Long depId);

    int deleteByPrimaryKey(Long fileId);

    int insert(RPanUserFile record);

    int insertSelective(RPanUserFile record);

    RPanUserFile selectByPrimaryKey(Long fileId);

    int updateByPrimaryKeySelective(RPanUserFile record);

    int updateByPrimaryKey(RPanUserFile record);

    List<RPanUserFileDisplayVO> selectRPanUserFileVOListBykeyword(@Param("depId") Long depId,
                                                                  @Param("keyword") String keyword,
                                                                  @Param("fileType") Integer fileType,
                                                                  @Param("delFlag") Integer delFlag);

    List<RPanUserFileDisplayVO> selectRPanUserFileVOListByUserId(@Param("depId") Long depId,
                                                                 @Param("fileTypeArray") List<Integer> fileTypeArray,
                                                                 @Param("parentId") Long parentId,
                                                                 @Param("delFlag") Integer delFlag);

    List<RPanUserFileDisplayVO> selectRPanUserFileVOList(@Param("fileTypeArray") List<Integer> fileTypeArray,
                                                         @Param("parentId") Long parentId,
                                                         @Param("delFlag") Integer delFlag);


    List<RPanUserFileVO> selectRPanUserFileVOListByUserIdAndFileTypeAndParentIdAndDelFlag(@Param("userId") Long userId,
                                                                                          @Param("fileTypeArray") List<Integer> fileTypeArray,
                                                                                          @Param("parentId") Long parentId,
                                                                                          @Param("delFlag") Integer delFlag);

    RPanUserFile selectByFileId(@Param("fileId") Long fileId);

    int selectCountByUserIdAndFilenameAndParentId(@Param("depId") Long depId,
                                                  @Param("filename") String filename,
                                                  @Param("parentId") Long parentId);

    int deleteBatch(@Param("idList") List<Long> idList);

    int deleteBatchReal(@Param("idList") List<Long> idList);

    int passeBatch(@Param("idList") List<Long> idList);

    int deleteById(Long id);

    int passFileById(Long id);

    List<RPanUserFile> selectFolderListByUserId(@Param("rootFileId") Long rootFileId);

    List<RPanUserFile> selectListByFileIdList(@Param("idList") List<Long> idList);

    int insertBatch(@Param("recordList") List<RPanUserFile> recordList);

    List<RPanUserFileSearchVO> selectRPanUserFileVOListByUserIdAndFilenameAndFileTypes(@Param("userId") Long userId, @Param("keyword") String keyword, @Param("fileTypeArray") List<Integer> fileTypeArray);

    RPanUserFileDisplayVO selectRPanUserFileVOByFileIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);

    List<RPanUserFile> selectAllListByParentId(@Param("parentId") Long parentId);

    List<RPanUserFile> selectAvailableListByParentId(@Param("parentId") Long parentId);

    List<RPanUserFileVO> selectAvailableRPanUserFileVOListByParentId(@Param("parentId") Long parentId);

    int updateUserFileDelFlagByFileIdListAndUserId(@Param("fileIdList") List<Long> fileIdList, @Param("userId") Long userId, @Param("depId") Long depId);

    int physicalDeleteBatch(@Param("fileIdList") List<Long> fileIdList, @Param("depId") Long depId);

    int selectCountByRealFileId(@Param("realFileId") Long realFileId);

    List<RPanUserFileVO> selectRPanUserFileVOListByFileIdList(@Param("fileIdList") List<Long> fileIdList);

    RPanUserFile selectTopFolderByUserId(Long depId);

    List<Long> selectAvailableFileIdListByParentId(@Param("parentId") Long parentId);

    List<FilePositionBO> selectFilePositionBOListByFileIds(@Param("fileIdList") List<Long> fileIdList);

}