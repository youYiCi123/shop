package com.jxm.file.service;


import com.jxm.file.dto.DashboardUserFileParam;
import com.jxm.file.entity.RPanUserFile;
import com.jxm.file.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 用户文件业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IUserFileService {

    List<RPanUserFileDisplayVO> filesForTable(Long parentId, String fileTypes, Long depId,Integer pageNum,Integer pageSize);

    List<RPanUserFileDisplayVO> list( Long pageType,Long parentId, String fileTypes, Long depId);

    List<RPanUserFileDisplayVO> filesForTable(Long parentId, String fileTypes, Long depId, Integer delFlag,Integer pageNum,Integer pageSize);

    List<RPanUserFileVO> list(String fileIds);

    void createFolder(Boolean isPageType,Long parentId, String folderName, Object loginUser);

    void createDepRootFolder(Long parentId, String folderName, Long userId,Long depId);

    void updateFilename(Long fileId, String newFilename, Object loginUser);

    void delete(String fileIds, Object loginUser);

    void upload(MultipartFile file, Long parentId, Object loginUser, String identifier, Long totalSize, String filename);

    FileChunkUploadVO uploadWithChunk(MultipartFile file, Long userId, String identifier, Integer totalChunks, Integer chunkNumber, Long totalSize, String filename);

    void download(Long fileId, HttpServletResponse response, Long userId);

    void download(Long fileId, HttpServletResponse response);

    List<FolderTreeNodeVO> getFolderTree(Long fileRootId, Long depId);

    void transfer(String fileIds, Long targetParentId, Long depId);

    void copy(String fileIds, Long targetParentId, Long depId);

    List<RPanUserFileSearchVO> search(String keyword, String fileTypes, Long userId);

    RPanUserFileDisplayVO detail(Long fileId, Long userId);

    List<BreadcrumbVO> getBreadcrumbs(Long fileId, Long depId);

    void preview(Long fileId, HttpServletResponse response, Long depId);

    void restoreUserFiles(String fileIds, HashMap<String, Integer> map);

    void physicalDeleteUserFiles(String fileIds, Long depId);

    List<RPanUserFileVO> allList(String fileIds);

    RPanUserFile getUserTopFileInfo(String depId);

    String getAllAvailableFileIdByFileIds(String fileIds);

    boolean checkAllUpFileAvailable(List<Long> fileIds);

    boolean secUpload(Long parentId, String filename, String md5,Object loginUser);

    CheckFileChunkUploadVO checkUploadWithChunk(Long userId, String identifier);

    void mergeChunks(String filename, String identifier, Long parentId, Long totalSize, Object loginUser);

    /**
     * 获取登录用户所属部门的文件类型数量信息
     * @return
     */
//    List<DashboardUserFileParam> getTheNumberOfFileTypes();

}
