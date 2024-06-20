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

    List<RPanUserFileDisplayVO> searchForName(@Param("depId") Long depId,
                                              @Param("keyword") String keyword);

    List<DashboardUserFileParam> getTheNumberOfFileTypes();

    int insert(RPanUserFile record);

    int insertSelective(RPanUserFile record);

    RPanUserFile selectByPrimaryKey(Long fileId);

    int updateByPrimaryKeySelective(RPanUserFile record);

    int updateByPrimaryKey(RPanUserFile record);

    List<RPanUserFileDisplayVO> selectRPanUserFileVOListBykeyword(@Param("depId") Long depId,
                                                                  @Param("keyword") String keyword,
                                                                  @Param("fileType") Integer fileType,
                                                                  @Param("delFlag") Integer delFlag);

    List<RPanUserFileDisplayVO> filesFromRecycleBin(@Param("userId") Long userId);

    List<RPanUserFileDisplayVO> selectRPanUserFileVOListByUserId(@Param("depId") Long depId,
                                                                 @Param("userId") Long userId,
                                                                 @Param("passFlag") Integer passFlag,
                                                                 @Param("fileTypeArray") List<Integer> fileTypeArray,
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

    int deleteFileById(@Param("fileId") Long fileId,@Param("userId") Long userId);

    int deleteBatchReal(@Param("idList") List<Long> idList);

    int passBatch(@Param("idList") List<Long> idList,@Param("passUserId") Long passUserId,@Param("passUserName") String passUserName);

    int recoveryBatch(@Param("idList") List<Long> idList);

    int deleteById(Long id);

    Long getUserByFileId(@Param("fileId") Long fileId);

    int passFileById(@Param("id") Long id,@Param("passUserId") Long passUserId,@Param("passUserName") String passUserName);

    int recoveryFile(@Param("id") Long id);

    String getTeamUserById(@Param("folderId") Long folderId);

    int updateTeamUser(@Param("folderId") Long folderId,@Param("participants") String participants);

    List<RPanUserFile> selectFolderListByUserId(@Param("depId") Long depId,@Param("userId") Long userId);

    List<RPanUserFile> selectListByFileIdList(@Param("idList") List<Long> idList);

    int insertBatch(@Param("recordList") List<RPanUserFile> recordList);

    List<RPanUserFileSearchVO> selectRPanUserFileVOListByUserIdAndFilenameAndFileTypes(@Param("userId") Long userId, @Param("keyword") String keyword, @Param("fileTypeArray") List<Integer> fileTypeArray);

    RPanUserFileDisplayVO selectRPanUserFileVOByFileIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);

    List<RPanUserFile> selectAllListByParentId(@Param("parentId") Long parentId);

    List<RPanUserFile> selectAvailableListByParentId(@Param("parentId") Long parentId);

    List<RPanUserFileVO> selectAvailableRPanUserFileVOListByParentId(@Param("parentId") Long parentId);

    int updateUserFileDelFlagByFileIdListAndUserId(@Param("fileIdList") List<Long> fileIdList, @Param("userId") Long userId, @Param("depId") Long depId);

    int physicalDeleteBatch(@Param("fileIdList") List<Long> fileIdList);

    int selectCountByRealFileId(@Param("realFileId") Long realFileId);

    List<RPanUserFileVO> selectRPanUserFileVOListByFileIdList(@Param("fileIdList") List<Long> fileIdList);

    RPanUserFile selectTopFolderByUserId(Long depId);

    List<Long> selectAvailableFileIdListByParentId(@Param("parentId") Long parentId);

    List<FilePositionBO> selectFilePositionBOListByFileIds(@Param("fileIdList") List<Long> fileIdList);

    Long searchRealFileId(Long fileId);

    List<Long> searchRealFileIds(@Param("idList") List<Long> idList);

    String getFileNameById(Long fileId);

    //获取所有到期文件id
    List<Long> selectExpireFileId();
}