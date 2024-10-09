package com.jxm.file.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.jxm.common.api.ResultCode;
import com.jxm.common.exception.Asserts;
import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.file.bo.FilePositionBO;
import com.jxm.file.constant.CommonConstant;
import com.jxm.file.constant.FileConstant;
import com.jxm.file.dto.*;
import com.jxm.file.entity.*;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.mapper.*;
import com.jxm.file.service.IFileService;
import com.jxm.file.service.IUserFileService;
import com.jxm.file.service.IUserSearchHistoryService;
import com.jxm.file.storage.LocalStorageConfig;
import com.jxm.file.storage.StorageManager;
import com.jxm.file.type.FileTypeDefiner;
import com.jxm.file.type.context.FileTypeContext;
import com.jxm.file.util.*;
import com.jxm.file.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户文件业务处理实现
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "userFileService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class UserFileServiceImpl implements IUserFileService {

    private static final Logger log = LoggerFactory.getLogger(UserFileServiceImpl.class);

    @Autowired
    @Qualifier(value = "rPanUserFileMapper")
    private RPanUserFileMapper rPanUserFileMapper;

    @Autowired
    @Qualifier(value = "rPanFileMapper")
    private RPanFileMapper rPanFileMapper;

    @Autowired
    @Qualifier(value = "fileService")
    private IFileService iFileService;

    @Autowired
    @Qualifier(value = "userSearchHistoryService")
    private IUserSearchHistoryService iUserSearchHistoryService;

    @Autowired
    @Qualifier(value = "storageManager")
    private StorageManager storageManager;

    @Autowired
    @Qualifier(value = "fileOperateLogMapper")
    private FileOperateLogMapper fileOperateLogMapper;

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private LocalStorageConfig localStorageConfig;

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private TeamUserMapper teamUserMapper;

    @Autowired
    private FileUserAuthMapper fileUserAuthMapper;

    @Override
    public List<RPanUserFileDisplayVO> searchForName(Long pageType, String keyword, Long depId) {
        List<RPanUserFileDisplayVO> rPanUserFileDisplayVOS = new ArrayList<>();
        if (Objects.equals(1L, pageType)) {
            rPanUserFileDisplayVOS = rPanUserFileMapper.searchForName(1L, keyword);
        } else {
            rPanUserFileDisplayVOS = rPanUserFileMapper.searchForName(depId, keyword);
        }
        return rPanUserFileDisplayVOS;
    }

    /**
     * 获取文件列表
     */
    @Override
    public List<RPanUserFileDisplayVO> filesForTable(Long pageType, String keyword, Integer fileType, Integer pageNum, Integer pageSize, Long depId) {
        List<RPanUserFileDisplayVO> rPanUserFileDisplayVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        if (Objects.equals(1L, pageType)) {
            rPanUserFileDisplayVOS = rPanUserFileMapper.selectRPanUserFileVOListBykeyword(1L, keyword, fileType, FileConstant.DelFlagEnum.NO.getCode());
        } else {
            rPanUserFileDisplayVOS = rPanUserFileMapper.selectRPanUserFileVOListBykeyword(depId, keyword, fileType, FileConstant.DelFlagEnum.NO.getCode());
        }
        if (CollectionUtils.isNotEmpty(rPanUserFileDisplayVOS)) {
            List<Long> parentIdList = rPanUserFileDisplayVOS.stream().map(RPanUserFileDisplayVO::getParentId).collect(Collectors.toList());
            List<FilePositionBO> filePositionBOList = rPanUserFileMapper.selectFilePositionBOListByFileIds(parentIdList);
            final Map<Long, String> filePositionMap = filePositionBOList.stream().collect(Collectors.toMap(FilePositionBO::getFileId, FilePositionBO::getFilename));
            rPanUserFileDisplayVOS.stream().forEach(rPanUserFileSearchVO -> rPanUserFileSearchVO.setParentFilename(filePositionMap.get(rPanUserFileSearchVO.getParentId())));
        }
        return rPanUserFileDisplayVOS;
    }

    @Override
    public List<RPanUserFileDisplayVO> filesFromRecycleBin(Integer pageNum, Integer pageSize, Long userId) {
        List<RPanUserFileDisplayVO> rPanUserFileDisplayVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        rPanUserFileDisplayVOS = rPanUserFileMapper.filesFromRecycleBin(userId);
        if (CollectionUtils.isNotEmpty(rPanUserFileDisplayVOS)) {
            List<Long> parentIdList = rPanUserFileDisplayVOS.stream().map(RPanUserFileDisplayVO::getParentId).collect(Collectors.toList());
            List<FilePositionBO> filePositionBOList = rPanUserFileMapper.selectFilePositionBOListByFileIds(parentIdList);
            final Map<Long, String> filePositionMap = filePositionBOList.stream().collect(Collectors.toMap(FilePositionBO::getFileId, FilePositionBO::getFilename));
            rPanUserFileDisplayVOS.stream().forEach(rPanUserFileSearchVO -> rPanUserFileSearchVO.setParentFilename(filePositionMap.get(rPanUserFileSearchVO.getParentId())));
        }
        return rPanUserFileDisplayVOS;
    }

    /**
     * 获取文件列表
     */
    @Override
    public List<RPanUserFileDisplayVO> list(Long pageType, Long parentId, String fileTypes, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        return list(pageType, parentId, fileTypes, userDepDto.getDepId(),userDepDto.getUserId(),FileConstant.DelFlagEnum.NO.getCode());
    }

    /**
     * 获取文件列表
     */
    public List<RPanUserFileDisplayVO> list(Long pageType, Long parentId, String fileTypes, Long depId,Long userId, Integer delFlag) {
        List<Integer> fileTypeArray = null;
        if (Objects.equals(CommonConstant.ZERO_LONG, parentId)||Objects.equals(-1L, parentId)) {
            return Lists.newArrayList();
        }
        if (!Objects.equals(fileTypes, FileConstant.ALL_FILE_TYPE)) {
            fileTypeArray = StringListUtil.string2IntegerList(fileTypes);
        }
        if (Objects.equals(1L, pageType)) {//企业文件预览
            return rPanUserFileMapper.selectRPanUserFileVOListByUserId(1L,userId, 1, fileTypeArray, parentId, delFlag);
        } else if (Objects.equals(2L, pageType)) {//部门文件预览
            return rPanUserFileMapper.selectRPanUserFileVOListByUserId(depId,userId ,1, fileTypeArray, parentId, delFlag);
        } else if (Objects.equals(3L, pageType)) {//企业文件审核
            return rPanUserFileMapper.selectRPanUserFileVOListByUserId(1L,userId, 0, fileTypeArray, parentId, delFlag);
        } else {//部门文件审核
            return rPanUserFileMapper.selectRPanUserFileVOListByUserId(depId,userId,0, fileTypeArray, parentId, delFlag);
        }
    }

    /**
     * 获取文件列表
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<RPanUserFileVO> list(String fileIds) {
        return rPanUserFileMapper.selectRPanUserFileVOListByFileIdList(StringListUtil.string2LongList(fileIds));
    }

    /**
     * 创建文件夹
     */
    @Override
    public void createFolder(Boolean isPageType,String teamFlag, Long parentId, String folderName,Integer folderType,String participants,Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        if (isPageType) {
            saveFolder(parentId, folderName, FileConstant.FolderFlagEnum.YES, null, null, userDepDto.getUserId(), userDepDto.getNickName(), null, userDepDto.getDepId(),folderType,participants,teamFlag);
        } else {
            saveFolder(parentId, folderName, FileConstant.FolderFlagEnum.YES, null, null, userDepDto.getUserId(), userDepDto.getNickName(), null, 1L,folderType,participants,"false");
        }

    }

    @Override
    public void createDepRootFolder(Long parentId, String folderName, Long userId, Long depId) {
        saveUserFile(parentId, folderName, FileConstant.FolderFlagEnum.YES, null, null, userId, "", null, depId);
    }

    @Override
    public void saveSet(Long fileId, Integer isWaterMater, Long loginUserId) {
        RPanUserFile originalUserFileInfo = getRPanUserFileByFileIdAndUserId(fileId);
        originalUserFileInfo.setWaterMaterFlag(isWaterMater);
        originalUserFileInfo.setUpdateUser(loginUserId);
        originalUserFileInfo.setUpdateTime(new Date());
        if (rPanUserFileMapper.updateByPrimaryKeySelective(originalUserFileInfo) != CommonConstant.ONE_INT) {
            Asserts.fail("文件设置失败");
        }
    }

    /**
     * 文件重命名
     */
    @Override
    public void updateFilename(Long fileId, String newFilename, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        RPanUserFile originalUserFileInfo = getRPanUserFileByFileIdAndUserId(fileId);
        if (Objects.equals(originalUserFileInfo.getFilename(), newFilename)) {
            Asserts.fail("请使用一个新名称");
        }
        if (rPanUserFileMapper.selectCountByUserIdAndFilenameAndParentId(userDepDto.getDepId(), newFilename, originalUserFileInfo.getParentId()) > CommonConstant.ZERO_INT) {
            Asserts.fail("名称已被占用");
        }
        originalUserFileInfo.setFilename(newFilename);
        originalUserFileInfo.setUpdateUser(userDepDto.getUserId());
        originalUserFileInfo.setUpdateTime(new Date());
        if (rPanUserFileMapper.updateByPrimaryKeySelective(originalUserFileInfo) != CommonConstant.ONE_INT) {
            Asserts.fail("文件重命名失败");
        }
    }

    /**
     * 删除文件(批量)
     */
    @Override
    public void delete(Long fileId, Long userId) {
//        List<Long> idList = StringListUtil.string2LongList(fileIds);
        if (rPanUserFileMapper.deleteFileById(fileId, userId) == 0) {
            Asserts.fail("删除失败");
        }
    }

    @Override
    public int deleteBatch(List<Long> idList) {
        List<Long> realFileIds = rPanUserFileMapper.searchRealFileIds(idList);
        if(!CollUtil.isEmpty(realFileIds)){
            realFileIds.stream().filter(fileId->!fileId.equals(0L)).forEach(this::physicalDeleteUserFiles);
        }
        return rPanUserFileMapper.deleteBatchReal(idList);
    }

    @Override
    public int passBatch(List<Long> idList, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        return rPanUserFileMapper.passBatch(idList, userDepDto.getUserId(), userDepDto.getNickName());
    }

    @Override
    public int recoveryBatch(List<Long> idList) {
        return rPanUserFileMapper.recoveryBatch(idList);
    }

    //回收站
    @Override
    public int recoveryFile(Long id) {
        return rPanUserFileMapper.recoveryFile(id);
    }

    @Override
    public int deleteFile(Long id) {
        //查询出真实地址然后删除
        Long realFileId = rPanUserFileMapper.searchRealFileId(id);
        if(!realFileId.equals(0L)){//单个文件
            String filePath = rPanFileMapper.filePathById(realFileId);
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("文件删除成功");
            } else {
                System.out.println("文件删除失败");
            }
            rPanFileMapper.deleteByPrimaryKey(realFileId);
        }else{//文件夹
            physicalDeleteUserFiles(id);
        }
        return rPanUserFileMapper.deleteById(id);
    }

    @Override
    public Long getUserByFileId(Long fileId) {
        return rPanUserFileMapper.getUserByFileId(fileId);
    }

    @Override
    public int passFile(Long id, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        return rPanUserFileMapper.passFileById(id, userDepDto.getUserId(), userDepDto.getNickName());
    }

    /**
     * 文件上传
     *
     * @param file
     * @param parentId
     * @param identifier
     * @param totalSize
     */
    @Override
    public void upload(MultipartFile file, Long parentId, UserDepDto userDepDto, String identifier, Long totalSize, String filename) {
        RPanFile rPanFile = uploadRealFile(file, userDepDto.getDepId(), identifier, totalSize, FileUtil.getFileSuffix(filename));
        saveUserFile(parentId, filename, FileConstant.FolderFlagEnum.NO, FileTypeContext.getFileTypeCode(filename), rPanFile.getFileId(), userDepDto.getUserId(), userDepDto.getNickName(), rPanFile.getFileSizeDesc(), userDepDto.getDepId());
    }

    /**
     * 文件分片上传
     *
     * @param file
     * @param userId
     * @param identifier
     * @param totalChunks
     * @param chunkNumber
     * @param totalSize
     * @param filename
     * @return
     */
    @Override
    public FileChunkUploadVO uploadWithChunk(MultipartFile file, Long userId, String identifier, Integer totalChunks, Integer chunkNumber, Long totalSize, String filename) {
        FileChunkUploadVO fileChunkUploadVO = new FileChunkUploadVO();
        fileChunkUploadVO.setMergeFlag(FileConstant.MergeFlag.NOT_READY.getCode());
        if (iFileService.saveWithChunk(file, userId, identifier, totalChunks, chunkNumber, totalSize, filename)) {
            fileChunkUploadVO.setMergeFlag(FileConstant.MergeFlag.READY.getCode());
        }
        return fileChunkUploadVO;
    }

    /**
     * 文件下载
     */
    @Override
    public void download(Long fileId, String waterMark,List<Integer> pagesToWatermark,HttpServletResponse response) {
        try {
            getRPanUserFileByFileIdAndUserId(fileId);
        } catch (Exception e) {
            Asserts.fail("您没有下载权限");
        }
        if (checkIsFolder(fileId)) {
            Asserts.fail("不能选择文件夹下载");
        }
        RPanUserFile rPanUserFile = rPanUserFileMapper.selectByPrimaryKey(fileId);
        if(rPanUserFile.getWaterMaterFlag().equals(1)&&waterMark.equals("")){
            Asserts.fail("请输入水印内容");
        }
        RPanFile rPanFile = iFileService.getFileDetail(rPanUserFile.getRealFileId());
        doDownload(rPanFile.getRealPath(), response, rPanUserFile.getFilename(), waterMark,pagesToWatermark);
    }

    @Override
    public void uploadLog(Long fileId, String fileName, Long userId) {
        fileOperateLogMapper.insert(fileId, fileName, userId, "上传", "");
    }

    @Override
    public void downloadLog(Long fileId, String fileName, Long userId, String waterMark) {
        fileOperateLogMapper.insert(fileId, fileName, userId, "下载", waterMark);
    }

    @Override
    public void deleteLog(Long fileId, String fileName, Long userId) {
        fileOperateLogMapper.insert(fileId, fileName, userId, "删除", "");
    }

    @Override
    public void updateFileReason(Long fileId, String reason) {
        fileOperateLogMapper.updateFileReason(fileId,reason);
    }

    @Override
    public void updateFilesReason(List<Long> fileIds, String reason) {
        fileOperateLogMapper.updateFilesReason(fileIds,reason);
    }

    /**
     * 获取文件夹树
     */
    @Override
    public List<FolderTreeNodeVO> getFolderTree(Long fileRootId,Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        List<RPanUserFile> folderList = new ArrayList<>();
        if (fileRootId != 1L) {//部门文件
            folderList = rPanUserFileMapper.selectFolderListByUserId(userDepDto.getDepId(),userDepDto.getUserId());
        } else {
            folderList = rPanUserFileMapper.selectFolderListByUserId(1L,userDepDto.getUserId());
        }
        return assembleFolderTree(folderList);
    }

    @Override
    public List<UmsAdminConcat> getTeamUser(Long folderId) {
        String teamUser = rPanUserFileMapper.getTeamUserById(folderId);
        String[] teamUserArray = teamUser.split(",");
        List<UmsAdminConcat> umsAdminConcatList=new ArrayList<>();
        Arrays.stream(teamUserArray).forEach(t->{
            UmsAdminConcat adminConcat = upstageService.getUmsAdminConcat(Long.parseLong(t)).getData();
            umsAdminConcatList.add(adminConcat);
        });
        return umsAdminConcatList;
    }

    @Override
    public int updateTeamUser(String[] teamUsers, Long folderId) {
        String participants = Arrays.stream(teamUsers).map(String::valueOf).collect(Collectors.joining(","));
        return rPanUserFileMapper.updateTeamUser(folderId,participants);
    }

    /**
     * 批量转移文件
     *
     * @param fileIds
     * @param targetParentId
     * @param depId
     */
    @Override
    public void transfer(String fileIds, Long targetParentId, Long depId) {
        if (!checkIsFolder(targetParentId, depId)) {
            Asserts.fail("请选择要转移到的文件夹");
        }
        if (!checkTargetParentIdAvailable(targetParentId, fileIds)) {
            Asserts.fail("要转移的文件中包含选中的目标文件夹,请重新选择");
        }
        // 查询所有要被转移的文件信息
        List<RPanUserFile> toBeTransferredFileInfoList = rPanUserFileMapper.selectListByFileIdList(StringListUtil.string2LongList(fileIds));
        toBeTransferredFileInfoList.stream().forEach(rPanUserFile -> transferOne(rPanUserFile, targetParentId));
    }

    /**
     * 批量复制文件
     *
     * @param fileIds
     * @param targetParentId
     * @param loginUser
     */
    @Override
    public void copy(Integer pageType, String fileIds, Long targetParentId, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        if (!checkIsFolder(targetParentId)) {
            Asserts.fail("请选择要复制到的文件夹");
        }
        if (!checkTargetParentIdAvailable(targetParentId, fileIds)) {
            Asserts.fail("要复制的文件中包含选中的目标文件夹,请重新选择");
        }
        if (pageType == 1) {//1企业
            doCopyUserFiles(fileIds, targetParentId, 1L, userDepDto.getUserId());
        } else {//2部门
            doCopyUserFiles(fileIds, targetParentId, userDepDto.getDepId(), userDepDto.getUserId());
        }
    }

    /**
     * 通过文件名搜索文件
     *
     * @param keyword
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileSearchVO> search(String keyword, String fileTypes, Long userId) {
        saveUserSearchHistory(keyword, userId);
        List<Integer> fileTypeArray = null;
        if (!Objects.equals(fileTypes, FileConstant.ALL_FILE_TYPE)) {
            fileTypeArray = StringListUtil.string2IntegerList(fileTypes);
        }
        List<RPanUserFileSearchVO> rPanUserFileSearchVOList = rPanUserFileMapper.selectRPanUserFileVOListByUserIdAndFilenameAndFileTypes(userId, keyword, fileTypeArray);
        if (CollectionUtils.isNotEmpty(rPanUserFileSearchVOList)) {
            List<Long> parentIdList = rPanUserFileSearchVOList.stream().map(RPanUserFileSearchVO::getParentId).collect(Collectors.toList());
            List<FilePositionBO> filePositionBOList = rPanUserFileMapper.selectFilePositionBOListByFileIds(parentIdList);
            final Map<Long, String> filePositionMap = filePositionBOList.stream().collect(Collectors.toMap(FilePositionBO::getFileId, FilePositionBO::getFilename));
            rPanUserFileSearchVOList.stream().forEach(rPanUserFileSearchVO -> rPanUserFileSearchVO.setParentFilename(filePositionMap.get(rPanUserFileSearchVO.getParentId())));
        }
        return rPanUserFileSearchVOList;
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @param userId
     * @return
     */
    @Override
    public RPanUserFileDisplayVO detail(Long fileId, Long userId) {
        return rPanUserFileMapper.selectRPanUserFileVOByFileIdAndUserId(fileId, userId);
    }


    /**
     * 获取面包屑列表
     */
    @Override
    public List<BreadcrumbVO> getBreadcrumbs(Long fileId, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        List<RPanUserFile> rPanUserFileList = rPanUserFileMapper.selectFolderListByUserId(userDepDto.getDepId(),userDepDto.getUserId());
        if (CollectionUtils.isEmpty(rPanUserFileList)) {
            return Lists.newArrayList();
        }
        Map<Long, BreadcrumbVO> breadcrumbVOTransferMap = rPanUserFileList.stream().collect(Collectors.toMap(RPanUserFile::getFileId, BreadcrumbVO::assembleBreadcrumbVO));
        List<BreadcrumbVO> breadcrumbVOList = Lists.newArrayList();
        BreadcrumbVO thisLevel = breadcrumbVOTransferMap.get(fileId);
        do {
            breadcrumbVOList.add(thisLevel);
            thisLevel = breadcrumbVOTransferMap.get(thisLevel.getParentId());
        } while (!Objects.isNull(thisLevel));
        Collections.reverse(breadcrumbVOList);
        return breadcrumbVOList;
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @param response
     */
    @Override
    public void preview(Long fileId, String userName, HttpServletResponse response) {
        RPanUserFile rPanUserFile = rPanUserFileMapper.selectByPrimaryKey(fileId);
        RPanFile fileDetail = iFileService.getFileDetail(rPanUserFile.getRealFileId());
        preview(fileDetail.getRealPath(), userName, response, fileDetail.getFilePreviewContentType());
    }

    /**
     * 批量还原用户文件的删除状态
     *
     * @param fileIds
     * @param map
     * @return
     */
    @Override
    public void restoreUserFiles(String fileIds, HashMap<String, Integer> map) {
        long userId = map.get("userId").longValue();
        long depId = map.get("depId").longValue();
        if (StringUtils.isBlank(fileIds) || Objects.isNull(userId)) {
            Asserts.fail("批量还原用户文件的删除状态失败");
        }
        List<Long> fileIdList = StringListUtil.string2LongList(fileIds);
        List<RPanUserFile> rPanUserFiles = rPanUserFileMapper.selectListByFileIdList(fileIdList);
        Set<String> cleanSet = rPanUserFiles.stream().map(rPanUserFile -> rPanUserFile.getParentId() + rPanUserFile.getFilename()).collect(Collectors.toSet());
        if (cleanSet.size() != rPanUserFiles.size()) {
            Asserts.fail("文件还原失败，该还原文件中存在同名文件，请逐个还原并重命名");
        }
        for (RPanUserFile rPanUserFile : rPanUserFiles) {
            if (rPanUserFileMapper.selectCountByUserIdAndFilenameAndParentId(rPanUserFile.getDepId(), rPanUserFile.getFilename(), rPanUserFile.getParentId()) != CommonConstant.ZERO_INT) {
                Asserts.fail("文件：" + rPanUserFile.getFilename() + "还原失败，该文件夹中已有相同名称的文件或文件夹，请重命名后重试");
            }
        }
        if (rPanUserFileMapper.updateUserFileDelFlagByFileIdListAndUserId(fileIdList, userId, depId) != fileIdList.size()) {
            Asserts.fail("批量还原用户文件的删除状态失败");
        }
    }

    /**
     * 物理删除用户文件
     */
    @Override
    public void physicalDeleteUserFiles(Long fileId) {
        List<RPanUserFile> rPanUserFileList = assembleAllToBeDeletedUserFileList(fileId);
        if(rPanUserFileList.size()==0){
            return;
        }
        List<Long> fileIdList = rPanUserFileList.stream().map(RPanUserFile::getFileId).collect(Collectors.toList());
        if (rPanUserFileMapper.physicalDeleteBatch(fileIdList) != fileIdList.size()) {
            Asserts.fail("物理删除用户文件失败");
        }
        Set<Long> realFileIdSet = assembleAllUnusedRealFileIdSet(rPanUserFileList);
        if (CollectionUtils.isEmpty(realFileIdSet)) {
            return;
        }
        List<Long> list = new ArrayList<>(realFileIdSet);
        List<String> filePaths = rPanFileMapper.filePathByIds(list);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("文件删除成功");
            } else {
                System.out.println("文件删除失败:" + filePath);
            }
        }
        rPanFileMapper.deleteByPrimaryKeys(list);
    }

    /**
     * 获取对应文件列表的所有文件以及子文件信息
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<RPanUserFileVO> allList(String fileIds) {
        if (StringUtils.isBlank(fileIds)) {
            Asserts.fail(ResultCode.VALIDATE_FAILED);
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectRPanUserFileVOListByFileIdList(StringListUtil.string2LongList(fileIds));
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            return Lists.newArrayList();
        }
        final List<RPanUserFileVO> allRPanUserFileVOList = Lists.newArrayList(rPanUserFileVOList);
        rPanUserFileVOList.stream().forEach(rPanUserFileVO -> findAllAvailableChildUserFile(allRPanUserFileVOList, rPanUserFileVO));
        return allRPanUserFileVOList;
    }

    /**
     * 获取用户的顶级文件信息
     *
     * @param depId
     * @return
     */
    @Override
    public RPanUserFile getUserTopFileInfo(String depId) {
        long depLongId = Long.parseLong(depId);
        return rPanUserFileMapper.selectTopFolderByUserId(depLongId);
    }

    /**
     * 通过文件id集合获取所有文件id和子文件id
     *
     * @param fileIds
     * @return
     */
    @Override
    public String getAllAvailableFileIdByFileIds(String fileIds) {
        List<Long> fileIdList = StringListUtil.string2LongList(fileIds);
        List<Long> allAvailableFileId = Lists.newArrayList(fileIdList);
        fileIdList.stream().forEach(fileId -> findAllAvailableFileIdByFileId(allAvailableFileId, fileId));
        return StringListUtil.longListToString(allAvailableFileId);
    }

    /**
     * 校验所有的父文件夹以及当前文件有效
     *
     * @param fileIds
     * @return
     */
    @Override
    public boolean checkAllUpFileAvailable(List<Long> fileIds) {
        for (Long fileId : fileIds) {
            if (!checkUpFileAvailable(fileId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 秒传文件
     *
     * @param parentId
     * @param filename
     * @param identifier
     * @return
     */
//    todo  selectByIdentifier从哪插入的数据？
    @Override
    public boolean secUpload(Integer pageType, Long parentId, String filename, String identifier, Object loginUser) {
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        List<RPanFile> rPanFileList = iFileService.selectByIdentifier(identifier);
        if (CollectionUtils.isEmpty(rPanFileList)) {
            return false;
        }
        Long depId = 1L;
        if (pageType != 1) {
            depId = userDepDto.getDepId();
        }
        RPanFile rPanFile = rPanFileList.get(CommonConstant.ZERO_INT);
        saveUserFile(parentId,
                filename,
                FileConstant.FolderFlagEnum.NO,
                FileTypeContext.getFileTypeCode(filename),
                rPanFile.getFileId(),
                userDepDto.getUserId(),
                userDepDto.getNickName(),
                rPanFile.getFileSizeDesc(),
                depId);
        return true;
    }

    /**
     * 分片上传并检查已上传的分片
     *
     * @param userId
     * @param identifier
     * @return
     */
    @Override
    public CheckFileChunkUploadVO checkUploadWithChunk(Long userId, String identifier) {
        CheckFileChunkUploadVO checkFileChunkUploadVO = new CheckFileChunkUploadVO();
        List<Integer> uploadedChunkNumbers = iFileService.getUploadedChunkNumbers(identifier, userId);
        checkFileChunkUploadVO.setUploadedChunks(uploadedChunkNumbers);
        return checkFileChunkUploadVO;
    }

    /**
     * 合并文件分片
     *
     * @param filename
     * @param identifier
     * @param parentId
     * @param totalSize
     */
    @Override
    public void mergeChunks(Integer pageType, String filename, String identifier, Long parentId, Long totalSize, String waterMarkFlag, String teamFlag,UserDepDto userDepDto) {
        RPanFile rPanFile = iFileService.mergeChunks(identifier, totalSize, userDepDto.getUserId(), filename);
        Integer waterMarkFlag1 = 0;
        if (waterMarkFlag.equals("true")) {
            waterMarkFlag1 = 1;
        }
        if (pageType == 1) {//企业
            saveUserFileWithWaterMark(parentId, filename, FileConstant.FolderFlagEnum.NO, FileTypeContext.getFileTypeCode(filename), rPanFile.getFileId(), userDepDto.getUserId(), userDepDto.getNickName(), rPanFile.getFileSizeDesc(), waterMarkFlag1,"false", 1L);
        } else {//部门
            saveUserFileWithWaterMark(parentId, filename, FileConstant.FolderFlagEnum.NO, FileTypeContext.getFileTypeCode(filename), rPanFile.getFileId(), userDepDto.getUserId(), userDepDto.getNickName(), rPanFile.getFileSizeDesc(), waterMarkFlag1,teamFlag, userDepDto.getDepId());
        }
    }

    @Override
    public List<DashboardUserFileParam> getTheNumberOfFileTypes() {
        List<DashboardUserFileParam> theNumberOfFileTypes = rPanUserFileMapper.getTheNumberOfFileTypes();
        if (theNumberOfFileTypes == null) {
            theNumberOfFileTypes = Collections.emptyList();
        }
        return theNumberOfFileTypes;
    }

    @Override
    public TeamUser getNotice(Long teamId) {
        return teamUserMapper.select(teamId);
    }

    @Override
    public FileUserAuth getUserFileAuth(Long fileId, Long userId) {
        return  fileUserAuthMapper.select(fileId,userId);
    }

    @Override
    public int editNotice(TeamUser teamUser) {
        TeamUser teamUser1 = teamUserMapper.select(teamUser.getTeamId());
        if(teamUser1==null){
            return  teamUserMapper.insert(teamUser);
        }else{
            return teamUserMapper.update(teamUser);
        }
    }

    @Override
    public int doUserFileAuth(FileUserAuth userFileAuth) {
        if(userFileAuth.getId().equals(0)){
            return  fileUserAuthMapper.insert(userFileAuth);
        }else{
            return fileUserAuthMapper.update(userFileAuth);
        }
    }

    @Override
    public String getFileNameById(Long fileId) {
        return rPanUserFileMapper.getFileNameById(fileId);
    }

    /******************************************************私有****************************************************/

    /**
     * 拼装用户文件实体信息
     *
     * @param parentId
     * @param userId
     * @param filename
     * @param folderFlag
     * @param fileType
     * @param realFileId
     * @param fileSizeDesc
     * @return
     */
    private RPanUserFile assembleRPanUserFile(Long parentId, Long userId, String nickName, Long depId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, Long realFileId, String fileSizeDesc, Integer waterMarkFlag,String teamFlag,Integer folderType,String participants) {
        RPanUserFile rPanUserFile = new RPanUserFile();
        long nextId = new UniqueIdGenerator(1, 1).nextId();
        rPanUserFile.setUserId(userId);
        rPanUserFile.setUserName(nickName);
        if(folderType!=0){
            rPanUserFile.setParentId(0L);
            rPanUserFile.setPassFlag(1);
        }else{
            rPanUserFile.setParentId(parentId);
            rPanUserFile.setPassFlag(0);
        }
        if(teamFlag.equals("true")){
            rPanUserFile.setPassFlag(1);
        }
        rPanUserFile.setFileId(nextId);
        rPanUserFile.setFilename(filename);
        rPanUserFile.setFileType(fileType);
        rPanUserFile.setFolderFlag(folderFlag.getCode());
        rPanUserFile.setFileSizeDesc(fileSizeDesc);
        rPanUserFile.setRealFileId(realFileId);
        rPanUserFile.setCreateUser(userId);
        rPanUserFile.setCreateTime(new Date());
        rPanUserFile.setUpdateUser(userId);
        rPanUserFile.setUpdateTime(new Date());
        rPanUserFile.setDepId(depId);
        rPanUserFile.setWaterMaterFlag(waterMarkFlag);
        rPanUserFile.setFolderType(folderType);
        rPanUserFile.setParticipants(participants);
        handleDuplicateFileName(rPanUserFile);//重复文件重命名
        uploadLog(nextId, filename, userId);
        return rPanUserFile;
    }

    /**
     * 保存用户文件信息
     */
//    private void saveUserFile(Long parentId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, Long realFileId, Long depId,String fileSizeDesc) {
//        if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, depId,filename, folderFlag, fileType, realFileId, fileSizeDesc)) != CommonConstant.ONE_INT) {
//            Asserts.fail("保存文件信息失败");
//        }
//    }

    /**
     * 创建部门时创建
     */
    private void saveUserFile(Long parentId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, Long realFileId, Long userId, String nickName, String fileSizeDesc, Long depId) {
        if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, nickName, depId, filename, folderFlag, fileType, realFileId, fileSizeDesc, 0,"false",0,"")) != CommonConstant.ONE_INT) {
            Asserts.fail("保存文件信息失败");
        }
    }

    /**
     * 创建文件夹
     */
    private void saveFolder(Long parentId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, Long realFileId, Long userId, String nickName, String fileSizeDesc, Long depId,Integer folderType,String participants,String teamFlag) {
        if(folderType!=0){
            if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, nickName, depId, filename, folderFlag, fileType, realFileId, fileSizeDesc, 0,"false",folderType,participants)) != CommonConstant.ONE_INT){
                Asserts.fail("保存文件信息失败");
            }
        }else{
            if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, nickName, depId, filename, folderFlag, fileType, realFileId, fileSizeDesc, 0,teamFlag,folderType,participants)) != CommonConstant.ONE_INT){
                Asserts.fail("保存文件信息失败");
            }
        }
    }

    private void saveUserFileWithWaterMark(Long parentId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, Long realFileId, Long userId, String nickName, String fileSizeDesc, Integer waterMarkFlag,String teamFlag, Long depId) {
        if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, nickName, depId, filename, folderFlag, fileType, realFileId, fileSizeDesc, waterMarkFlag,teamFlag,0,"")) != CommonConstant.ONE_INT) {
            Asserts.fail("保存文件信息失败");
        }
    }

    /**
     * 处理重复文件名
     *
     * @param rPanUserFile
     */
    private void handleDuplicateFileName(RPanUserFile rPanUserFile) {
        String newFileName = rPanUserFile.getFilename(),
                newFileNameWithoutSuffix,
                newFileNameSuffix;
        int newFileNamePointPosition = newFileName.lastIndexOf(CommonConstant.POINT_STR);
        if (newFileNamePointPosition == CommonConstant.MINUS_ONE_INT) {
            newFileNameWithoutSuffix = newFileName;
            newFileNameSuffix = CommonConstant.EMPTY_STR;
        } else {
            newFileNameWithoutSuffix = newFileName.substring(CommonConstant.ZERO_INT, newFileNamePointPosition);
            newFileNameSuffix = FileUtil.getFileSuffix(newFileName);
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectRPanUserFileVOListByUserIdAndFileTypeAndParentIdAndDelFlag(rPanUserFile.getUserId(), null, rPanUserFile.getParentId(), FileConstant.DelFlagEnum.NO.getCode());
        boolean noDuplicateFileNameFlag = rPanUserFileVOList.stream().noneMatch(rPanUserFileVO -> rPanUserFile.getFilename().equals(rPanUserFileVO.getFilename()));
        if (noDuplicateFileNameFlag) {
            return;
        }
        List<String> duplicateFileNameList = rPanUserFileVOList.stream()
                .map(RPanUserFileVO::getFilename)
                .filter(fileName -> fileName.startsWith(newFileNameWithoutSuffix))
                .filter(fileName -> {
                    int pointPosition = fileName.lastIndexOf(CommonConstant.POINT_STR);
                    String fileNameSuffix = CommonConstant.EMPTY_STR;
                    if (pointPosition != CommonConstant.MINUS_ONE_INT) {
                        fileNameSuffix = FileUtil.getFileSuffix(fileName);
                    }
                    return Objects.equals(newFileNameSuffix, fileNameSuffix);
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(duplicateFileNameList)) {
            return;
        }
        newFileName = new StringBuilder(newFileNameWithoutSuffix)
                .append(FileConstant.CN_LEFT_PARENTHESES_STR)
                .append((duplicateFileNameList.size()))
                .append(FileConstant.CN_RIGHT_PARENTHESES_STR)
                .append(newFileNameSuffix)
                .toString();
        rPanUserFile.setFilename(newFileName);
    }


    /**
     * 执行下载文件
     */
    private void doDownload(String filePath, HttpServletResponse response, String filename, String waterMark,List<Integer> pagesToWatermark) {
        addCommonResponseHeader(response, FileConstant.APPLICATION_OCTET_STREAM_STR);
        try {
            response.setHeader(FileConstant.CONTENT_DISPOSITION_STR, FileConstant.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(filename.getBytes(FileConstant.GB2312_STR), FileConstant.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            log.error("下载文件失败", e);
        }
        read2OutputStream(filePath, waterMark,pagesToWatermark,0,response);
    }

    /**
     * 补全要复制的文件列表
     *
     * @param toBeCopiedFileInfoList
     * @param targetParentId
     * @param userId
     */
    private void complementToBeCopiedFileInfoList(final List<RPanUserFile> toBeCopiedFileInfoList, final Long targetParentId, final Long depId, final Long userId) {
        final List<RPanUserFile> allChildUserFileList = Lists.newArrayList();
        toBeCopiedFileInfoList.stream().forEach(rPanUserFile -> {
            Long fileId = rPanUserFile.getFileId(),
                    newFileId = new UniqueIdGenerator(1, 1).nextId();
            rPanUserFile.setParentId(targetParentId);
            rPanUserFile.setUserId(userId);
            rPanUserFile.setFileId(newFileId);
            rPanUserFile.setCreateUser(userId);
            rPanUserFile.setCreateTime(new Date());
            rPanUserFile.setUpdateUser(userId);
            rPanUserFile.setUpdateTime(new Date());
            rPanUserFile.setDepId(depId);
            handleDuplicateFileName(rPanUserFile);
            if (checkIsFolder(rPanUserFile)) {
                assembleAllChildUserFile(allChildUserFileList, fileId, newFileId, depId, userId);
            }
        });
        toBeCopiedFileInfoList.addAll(allChildUserFileList);
    }

    /**
     * 文件预览
     */
    public void preview(String filePath, String userName, HttpServletResponse response, String filePreviewContentType) {
        addCommonResponseHeader(response, filePreviewContentType);
        read2OutputStream(filePath, userName,Arrays.asList(),1, response);
    }

    /**
     * 添加公用的响应头
     *
     * @param response
     * @param contentTypeValue
     */
    private void addCommonResponseHeader(HttpServletResponse response, String contentTypeValue) {
        response.reset();
        HttpUtil.addCorsResponseHeader(response);
        if (contentTypeValue.equals("text/plain"))
            contentTypeValue = contentTypeValue + ";charset=utf-8";
        response.setHeader(FileConstant.CONTENT_TYPE_STR, contentTypeValue);
        response.setContentType(contentTypeValue);
    }

    /**
     * 文件写入响应实体
     */
    private void read2OutputStream(String filePath, String waterMark,List<Integer> pagesToWatermark,Integer readFlag, HttpServletResponse response) {
        try {
            storageManager.read2OutputStream(filePath, waterMark, pagesToWatermark,readFlag,response.getOutputStream());
        } catch (Exception e) {
            log.error("文件写入响应实体失败", e);
        }
    }

    /**
     * 转移一个文件
     *
     * @param rPanUserFile
     * @param targetParentId
     */
    private void transferOne(RPanUserFile rPanUserFile, Long targetParentId) {
        if (rPanUserFile.getParentId().equals(targetParentId)) {
            return;
        }
        rPanUserFile.setParentId(targetParentId);
        rPanUserFile.setUpdateUser(rPanUserFile.getCreateUser());
        rPanUserFile.setUpdateTime(new Date());
        handleDuplicateFileName(rPanUserFile);
        // 修改文件信息
        if (rPanUserFileMapper.updateByPrimaryKeySelective(rPanUserFile) != CommonConstant.ONE_INT) {
            log.error("文件转移失败,文件名称为:{}", rPanUserFile.getFilename());
            Asserts.fail("文件转移失败");
        }
    }

    /**
     * 检查目标文件是不是子文件夹
     *
     * @param targetParentId
     * @param rPanUserFile
     * @return
     */
    private boolean checkIsChildFolder(Long targetParentId, RPanUserFile rPanUserFile) {
        if (checkIsFolder(rPanUserFile)) {
            List<RPanUserFile> allChildrenFile = Lists.newArrayList();
            findAllChildUserFile(allChildrenFile, rPanUserFile.getFileId());
            if (!CollectionUtils.isEmpty(allChildrenFile)) {
                List<Long> childFolderIdList = allChildrenFile.stream().filter(this::checkIsFolder).map(RPanUserFile::getFileId).collect(Collectors.toList());
                if (childFolderIdList.contains(targetParentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 上传真实文件
     *
     * @param file
     * @param depId
     * @param identifier
     * @param totalSize
     * @return
     */
    private RPanFile uploadRealFile(MultipartFile file, Long depId, String identifier, Long totalSize, String suffix) {
        return iFileService.save(file, depId, identifier, totalSize, suffix);
    }

    /**
     * 拼装文件夹树
     *
     * @param folderList
     * @return
     */
    private List<FolderTreeNodeVO> assembleFolderTree(List<RPanUserFile> folderList) {
        if (CollectionUtils.isEmpty(folderList)) {
            return Lists.newArrayList();
        }
        List<FolderTreeNodeVO> folderTreeNodeList = folderList.stream().map(FolderTreeNodeVO::assembleFolderTreeNode).collect(Collectors.toList());
        Map<Long, List<FolderTreeNodeVO>> directoryTreeNodeParentGroup = folderTreeNodeList.stream().collect(Collectors.groupingBy(FolderTreeNodeVO::getParentId));
        folderTreeNodeList.stream().forEach(node -> {
            List<FolderTreeNodeVO> children = directoryTreeNodeParentGroup.get(node.getId());
            if (!CollectionUtils.isEmpty(children)) {
                node.setChildren(children);
            }
        });
//        return new ArrayList<>(folderTreeNodeList);
        return folderTreeNodeList.stream().filter(node -> Objects.equals(CommonConstant.ZERO_LONG, node.getParentId())).collect(Collectors.toList());
    }

    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @return
     */
    private boolean checkIsFolder(Long fileId) {
        return checkIsFolder(rPanUserFileMapper.selectByPrimaryKey(fileId));
    }

    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @param depId
     * @return
     */
    private boolean checkIsFolder(Long fileId, Long depId) {
        return checkIsFolder(rPanUserFileMapper.selectByFileId(fileId));
    }

    /**
     * 校验是不是文件夹
     *
     * @param rPanUserFile
     * @return
     */
    private boolean checkIsFolder(RPanUserFile rPanUserFile) {
        if (Objects.isNull(rPanUserFile)) {
            Asserts.fail("文件不存在");
        }
        return Objects.equals(FileConstant.FolderFlagEnum.YES.getCode(), rPanUserFile.getFolderFlag());
    }

    /**
     * 校验是否是文件夹
     *
     * @param rPanUserFileVO
     * @return
     */
    private boolean checkIsFolder(RPanUserFileVO rPanUserFileVO) {
        if (Objects.isNull(rPanUserFileVO)) {
            Asserts.fail("文件不存在");
        }
        return Objects.equals(FileConstant.FolderFlagEnum.YES.getCode(), rPanUserFileVO.getFolderFlag());
    }

    /**
     * 查找并拼装子文件
     *
     * @param allChildUserFileList
     * @param parentUserFileId
     * @param newParentUserFileId
     * @param userId
     * @return
     */
    private void assembleAllChildUserFile(final List<RPanUserFile> allChildUserFileList, Long parentUserFileId, Long newParentUserFileId, Long depId, Long userId) {
        List<RPanUserFile> childUserFileList = rPanUserFileMapper.selectAvailableListByParentId(parentUserFileId);
        if (CollectionUtils.isEmpty(childUserFileList)) {
            return;
        }
        childUserFileList.stream().forEach(childUserFile -> {
            Long fileId = childUserFile.getFileId(),
                    newFileId = new UniqueIdGenerator(1, 1).nextId();
            childUserFile.setParentId(newParentUserFileId);
            childUserFile.setUserId(userId);
            childUserFile.setFileId(newFileId);
            childUserFile.setCreateUser(userId);
            childUserFile.setCreateTime(new Date());
            childUserFile.setUpdateUser(userId);
            childUserFile.setUpdateTime(new Date());
            childUserFile.setDepId(depId);
            allChildUserFileList.add(childUserFile);
            if (checkIsFolder(childUserFile)) {
                assembleAllChildUserFile(allChildUserFileList, fileId, newFileId, depId, userId);
            }
        });
    }

    /**
     * 校验是不是该物理文件还在使用
     *
     * @param realFileId
     * @return
     */
    private boolean checkRealFileUsed(Long realFileId) {
        return rPanUserFileMapper.selectCountByRealFileId(realFileId) > CommonConstant.ZERO_INT;
    }

    /**
     * 查询文件的所有子节点
     */
    private void findAllChildUserFile(final List<RPanUserFile> allChildUserFileList, Long parentId) {
        List<RPanUserFile> childUserFileList = rPanUserFileMapper.selectAllListByParentId(parentId);
        if (CollectionUtils.isEmpty(childUserFileList)) {
            return;
        }
        allChildUserFileList.addAll(childUserFileList);
        childUserFileList.stream()
                .filter(rPanUserFile -> checkIsFolder(rPanUserFile))
                .forEach(rPanUserFile -> findAllChildUserFile(allChildUserFileList, rPanUserFile.getFileId()));
    }

    /**
     * 保存用户搜索关键字信息
     *
     * @param keyword
     * @param userId
     */
    private void saveUserSearchHistory(String keyword, Long userId) {
        iUserSearchHistoryService.save(keyword, userId);
    }

    /**
     * 拼装所有的与删除的用户文件信息列表
     */
    private List<RPanUserFile> assembleAllToBeDeletedUserFileList(Long fileId) {
        List<RPanUserFile> rPanUserFileList = rPanUserFileMapper.selectAllListByParentId(fileId);
        final List<RPanUserFile> allChildUserFileList = Lists.newArrayList();
        rPanUserFileList.stream()
                .filter(rPanUserFile -> checkIsFolder(rPanUserFile))
                .forEach(rPanUserFile -> findAllChildUserFile(allChildUserFileList, rPanUserFile.getFileId()));
        rPanUserFileList.addAll(allChildUserFileList);
        return rPanUserFileList;
    }

    /**
     * 拼装要删除的真实文件id列表
     *
     * @param rPanUserFileList
     * @return
     */
    private Set<Long> assembleAllUnusedRealFileIdSet(List<RPanUserFile> rPanUserFileList) {
        Set<Long> realFileIdSet = rPanUserFileList.stream()
                .filter(rPanUserFile -> !checkIsFolder(rPanUserFile))
                .filter(rPanUserFile -> !checkRealFileUsed(rPanUserFile.getRealFileId()))
                .map(RPanUserFile::getRealFileId)
                .collect(Collectors.toSet());
        return realFileIdSet;
    }

    /**
     * 保存复制文件
     */
    private void doCopyUserFiles(String fileIds, Long targetParentId, Long depId, Long userId) {
        // 查询所有要被复制的文件信息
        List<RPanUserFile> toBeCopiedFileInfoList = rPanUserFileMapper.selectListByFileIdList(StringListUtil.string2LongList(fileIds));
        complementToBeCopiedFileInfoList(toBeCopiedFileInfoList, targetParentId, depId, userId);
        // 批量新增文件信息
        if (rPanUserFileMapper.insertBatch(toBeCopiedFileInfoList) != toBeCopiedFileInfoList.size()) {
            Asserts.fail("文件复制失败");
        }
    }

    /**
     * 递归查找所有子文件信息
     *
     * @param allRPanUserFileVOList
     * @param rPanUserFileVO
     */
    private void findAllAvailableChildUserFile(List<RPanUserFileVO> allRPanUserFileVOList, RPanUserFileVO rPanUserFileVO) {
        if (!checkIsFolder(rPanUserFileVO)) {
            return;
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectAvailableRPanUserFileVOListByParentId(rPanUserFileVO.getFileId());
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            return;
        }
        allRPanUserFileVOList.addAll(rPanUserFileVOList);
        rPanUserFileVOList.stream().forEach(rPanUserFileVOItem -> findAllAvailableChildUserFile(allRPanUserFileVOList, rPanUserFileVOItem));
    }

    /**
     * 根据文件id查询对应的文件信息
     */
    private RPanUserFile getRPanUserFileByFileIdAndUserId(Long fileId) {
        RPanUserFile originalUserFileInfo = rPanUserFileMapper.selectByPrimaryKey(fileId);
        if (Objects.isNull(originalUserFileInfo)) {
            Asserts.fail("此文件已不存在");
        }
        if (Objects.equals(FileConstant.DelFlagEnum.YES.getCode(), originalUserFileInfo.getDelFlag())) {
            Asserts.fail("此文件已不存在");
        }
        return originalUserFileInfo;
    }

    /**
     * 校验目标文件夹是否合法
     * 1、 不是选中的文件夹
     * 2、 不是选中文件夹的子文件夹
     *
     * @param targetParentId
     * @param fileIds
     * @return
     */
    private boolean checkTargetParentIdAvailable(Long targetParentId, String fileIds) {
        List<Long> fileIdList = StringListUtil.string2LongList(fileIds);
        if (fileIdList.contains(targetParentId)) {
            return false;
        }
        for (Long fileId : fileIdList) {
            if (checkIsChildFolder(targetParentId, rPanUserFileMapper.selectByPrimaryKey(fileId))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 递归查询所有有效的子节点id
     *
     * @param allAvailableFileId
     * @param fileId
     */
    private void findAllAvailableFileIdByFileId(List<Long> allAvailableFileId, Long fileId) {
        if (!checkIsFolder(fileId)) {
            return;
        }
        List<Long> childrenFileIds = rPanUserFileMapper.selectAvailableFileIdListByParentId(fileId);
        if (CollectionUtils.isEmpty(childrenFileIds)) {
            return;
        }
        allAvailableFileId.addAll(childrenFileIds);
        childrenFileIds.stream().forEach(childrenFileId -> findAllAvailableFileIdByFileId(allAvailableFileId, childrenFileId));
    }

    /**
     * 校验当前文件和所有父文件夹是否均正常
     *
     * @param fileId
     * @return
     */
    private boolean checkUpFileAvailable(Long fileId) {
        RPanUserFile rPanUserFile = rPanUserFileMapper.selectByPrimaryKey(fileId);
        if (rPanUserFile.getDelFlag().equals(FileConstant.DelFlagEnum.YES.getCode())) {
            return false;
        }
        if (rPanUserFile.getParentId().equals(CommonConstant.ZERO_LONG)) {
            return true;
        }
        return checkUpFileAvailable(rPanUserFile.getParentId());
    }

    @Override
    public void getALLExpireFile() {
        List<Long> idList = rPanUserFileMapper.selectExpireFileId();
        //获取所有到期文件地址
        idList.stream().filter(t->!t.equals(0L)).forEach(this::physicalDeleteUserFiles);
        rPanUserFileMapper.deleteBatchReal(idList);
    }

    @Override
    public void depToEnterprise(Long fileId, Long userId, Long targetParentId) {
        //根据文件id获取真实文件id
        RPanUserFile rPanUserFile = rPanUserFileMapper.selectByFileId(fileId);
        RPanFile rPanFile = rPanFileMapper.selectByPrimaryKey(rPanUserFile.getRealFileId());
        //文件从旧地址 复制到新地址
        //rPanFile.getRealPath() ===>newRealPath
        String newRealPath = new StringBuffer(localStorageConfig.getRootFilePath())
                .append(File.separator)
                .append(DateUtil.getYear())
                .append(File.separator)
                .append(DateUtil.getMonth())
                .append(File.separator)
                .append(DateUtil.getDay())
                .append(File.separator)
                .append(UUIDUtil.getUUID())
                .append(FileUtil.getFileSuffix(rPanUserFile.getFilename()))
                .toString();
        Path sourcePath = Paths.get(rPanFile.getRealPath());
        Path destinationPath = Paths.get(newRealPath);
        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件复制成功！");
        } catch (IOException e) {
            System.out.println("文件复制失败: " + e.getMessage());
        }

        long realFileId = new UniqueIdGenerator(1, 1).nextId();
        //新增文件和用户关系表 / 新增文件表
        RPanUserFile newRPanUserFile = new RPanUserFile();
        newRPanUserFile.setFileId(new UniqueIdGenerator(1, 1).nextId());
        newRPanUserFile.setWaterMaterFlag(rPanUserFile.getWaterMaterFlag());
        newRPanUserFile.setCreateTime(rPanUserFile.getCreateTime());
        newRPanUserFile.setCreateUser(rPanUserFile.getCreateUser());
        newRPanUserFile.setUserId(userId);
        newRPanUserFile.setUserName(rPanUserFile.getUserName());
        newRPanUserFile.setDelFlag(0);
        newRPanUserFile.setDepId(1L);
        newRPanUserFile.setFilename(rPanUserFile.getFilename());
        newRPanUserFile.setFileSizeDesc(rPanUserFile.getFileSizeDesc());
        newRPanUserFile.setFileType(rPanUserFile.getFileType());
        newRPanUserFile.setFolderFlag(rPanUserFile.getFolderFlag());
        newRPanUserFile.setPassFlag(0);
        newRPanUserFile.setRealFileId(realFileId);
        newRPanUserFile.setUpdateTime(new Date());
        newRPanUserFile.setUpdateUser(userId);
        newRPanUserFile.setParentId(targetParentId);
        newRPanUserFile.setPassTime(rPanUserFile.getPassTime());
        newRPanUserFile.setPassUserId(rPanUserFile.getPassUserId());
        newRPanUserFile.setPassUserName(rPanUserFile.getPassUserName());
        rPanUserFileMapper.insert(newRPanUserFile);
        /////////////////////////////
        RPanFile newRPanFile = new RPanFile();
        newRPanFile.setFileId(realFileId);
        newRPanFile.setCreateTime(new Date());
        newRPanFile.setCreateUser(userId);
        newRPanFile.setFilename(FileUtil.getFilename(newRealPath));
        newRPanFile.setFilePreviewContentType(rPanFile.getFilePreviewContentType());
        newRPanFile.setFileSize(rPanFile.getFileSize());
        newRPanFile.setFileSuffix(rPanFile.getFileSuffix());
        newRPanFile.setIdentifier("");
        newRPanFile.setRealPath(newRealPath);
        newRPanFile.setFileSizeDesc(rPanFile.getFileSizeDesc());
        rPanFileMapper.insert(newRPanFile);
    }
}
