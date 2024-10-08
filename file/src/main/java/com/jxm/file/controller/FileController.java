package com.jxm.file.controller;

import cn.hutool.json.JSONUtil;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.file.dto.*;
import com.jxm.file.entity.*;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.mapper.RPanUserFileMapper;
import com.jxm.file.po.*;
import com.jxm.file.service.FileOperateService;
import com.jxm.file.service.IUserFileService;
import com.jxm.file.service.MessageService;
import com.jxm.file.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目文件相关rest接口返回
 */
@RestController
@Validated
@Api(tags = "文件")
public class FileController {

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "rPanUserFileMapper")
    private RPanUserFileMapper rPanUserFileMapper;

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileOperateService fileOperateService;


    @ApiOperation(
            value = "搜索文件",
            notes = "该接口提供了根据文件名搜索文件的功能"
    )
    @RequestMapping(value = "/searchForName", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<RPanUserFileDisplayVO>> searchForName(@RequestParam(value = "pageType", required = false) Long pageType,
                                                                   @RequestParam(value = "keyword", required = false) String keyword) throws ParseException {
        List<RPanUserFileDisplayVO> list = iUserFileService.searchForName(pageType, keyword, getLoginDepId());
        return CommonResult.success(list);
    }


    @ApiOperation(
            value = "获取回收站文件列表",
            notes = "该接口提供了获取文件列表的功能"
    )
    @RequestMapping(value = "/filesFromRecycleBin", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<RPanUserFileDisplayVO>> filesFromRecycleBin(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws ParseException {
        List<RPanUserFileDisplayVO> list = iUserFileService.filesFromRecycleBin(pageNum, pageSize, getLoginUserId());
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation(
            value = "获取文件列表",
            notes = "该接口提供了获取文件列表的功能"
    )
    @RequestMapping(value = "/filesForTable", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<RPanUserFileDisplayVO>> filesForTable(@RequestParam(value = "pageType", required = false) Long pageType,
                                                                         @RequestParam(value = "keyword", required = false) String keyword,
                                                                         @RequestParam(value = "fileType", required = false) Integer fileType,
                                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws ParseException {
        List<RPanUserFileDisplayVO> list = iUserFileService.filesForTable(pageType, keyword, fileType, pageNum, pageSize, getLoginDepId());
        return CommonResult.success(CommonPage.restPage(list));
    }


    @ApiOperation(
            value = "获取文件日志列表",
            notes = "该接口提供了获取文件日志列表的功能"
    )
    @RequestMapping(value = "/filesOperateLogs/select", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<FileOperateLogDetail>> filesOperateLogs(@RequestParam(defaultValue = "") String[] date,
                                                                           @RequestParam(value = "userId", required = false) Long userId,
                                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        CommonPage<FileOperateLogDetail> list = fileOperateService.getFileOperateLog(startDate, endDate, userId, pageNum, pageSize);
        return CommonResult.success(list);
    }

    @ApiOperation(
            value = "获取我的上传文件列表",
            notes = "该接口提供了获取我的上传文件的功能"
    )
    @RequestMapping(value = "/myUploadFileStatus/select", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MyUploadFileStatus>> getMyUploadFileStatus(@RequestParam(defaultValue = "") String[] date,
                                                                              @RequestParam(value = "keyword", required = false) String keyword,
                                                                              @RequestParam(value = "userId", required = false) Long userId,
                                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        CommonPage<MyUploadFileStatus> list = fileOperateService.getMyUploadFileStatus(startDate, endDate, userId, keyword,pageNum, pageSize);
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/filesOperateLogs/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteLog(@PathVariable Long id) {
        //删除培训信息
        int count = fileOperateService.deleteLog(id);
        if (count < 0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }

    @RequestMapping(value = "/filesOperateLogs/deleteBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatchLog(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList = Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = fileOperateService.deleteBatchLog(idList);
        if (count < 0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }


    @RequestMapping(value = "/files", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<RPanUserFileDisplayVO>> list(@RequestParam(value = "pageType", required = false) Long pageType,
                                                          @RequestParam(value = "parentId", required = false) Long parentId,
                                                          @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes
    ) throws ParseException {
        List<RPanUserFileDisplayVO> list = iUserFileService.list(pageType, parentId, fileTypes, getLoginUser());
        return CommonResult.success(list);
    }

    @ApiOperation(
            value = "新建文件夹",
            notes = "该接口提供了新建文件夹的功能"
    )
    @PostMapping("file/folder")
    public CommonResult createFolder(@Validated @RequestBody CreateFolderPO createFolderPO) throws ParseException {
        String participants = "";
        if(createFolderPO.getFolderType()==1){
            participants = createFolderPO.getThisDepParticipants().stream().map(String::valueOf).collect(Collectors.joining(","));
        }else if(createFolderPO.getFolderType()==2){
            participants = createFolderPO.getCrossDepParticipants().stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        // todo 获取父系文件夹
        iUserFileService.createFolder(createFolderPO.isPageType(),createFolderPO.getTeamFlag(), createFolderPO.getParentId(), createFolderPO.getFolderName(),createFolderPO.getFolderType(),participants, getLoginUser());
        return CommonResult.success();
    }

    @PostMapping("file/folder/createDepRootFolder")
    public CommonResult createDepRootFolder(@RequestParam("parentId") Long parentId, @RequestParam("folderName") String folderName,
                                            @RequestParam("userId") Long userId, @RequestParam("depId") Long depId) {
        iUserFileService.createDepRootFolder(parentId, folderName, userId, depId);
        return CommonResult.success();
    }

    @ApiOperation(
            value = "文件基础设置",
            notes = "该接口提供了文件基础设置的功能"
    )
    @PostMapping("/file/saveSet")
    public CommonResult saveSet(@Validated @RequestBody BasicSetFilePO basicSetFilePO) throws ParseException {
        Long loginUserId = getLoginUserId();
        //文件创建人本人、文件创建人的部门长,才可以设置文件
        Long createUserId = iUserFileService.getUserByFileId(basicSetFilePO.getFileId());
        Long depHeadId = upstageService.selectDepHeadIdByUser(createUserId).getData();
        if (loginUserId.equals(createUserId) || loginUserId.equals(depHeadId)) {
            iUserFileService.saveSet(basicSetFilePO.getFileId(), basicSetFilePO.getIsWaterMater(), loginUserId);
            return CommonResult.success();
        }else{
            return CommonResult.failed("创建人本人或其部门长,才可设置该文件");
        }
    }


    @ApiOperation(
            value = "文件重命名",
            notes = "该接口提供了文件重命名的功能"
    )
    @PostMapping("/file/updateFilename")
    public CommonResult updateFilename(@Validated @RequestBody UpdateFileNamePO updateFileNamePO) throws ParseException {
        Object loginUser = getLoginUser();
        iUserFileService.updateFilename(updateFileNamePO.getFileId(), updateFileNamePO.getNewFilename(), loginUser);
        return CommonResult.success();
    }

    @ApiOperation(
            value = "删除文件(单个)",
            notes = "该接口提供了删除文件(单个)的功能"
    )
    @PostMapping("/file/delete")
    public CommonResult delete(Long fileId) throws ParseException {
        Long userId = getLoginUserId();
        //文件创建人本人、文件创建人的部门长,才可以删除文件
        Long createUserId = iUserFileService.getUserByFileId(fileId);
        Long depHeadId = upstageService.selectDepHeadIdByUser(createUserId).getData();
        if (userId.equals(createUserId) || userId.equals(depHeadId)) {
            iUserFileService.delete(fileId, userId);
            String fileName = iUserFileService.getFileNameById(fileId);
            iUserFileService.deleteLog(fileId, fileName, userId);
            return CommonResult.success();
        } else {
            return CommonResult.failed("创建人本人或其部门长,才可该删除文件");
        }
    }

    @ApiOperation(
            value = "删除文件(批量)----预览页面",
            notes = "该接口提供了删除文件(批量)的功能"
    )
    @PostMapping("/file/delete/multiply")
    public CommonResult deleteMultiply(@RequestBody List<FileSimplifyDto> multiplyFileList) throws ParseException {
        Long userId = getLoginUserId();
        //存储未能被删除的文件（权限或其它原因）
        String tempFileStr = "";
        for(FileSimplifyDto t :multiplyFileList){
            //文件创建人本人、文件创建人的部门长,才可以删除文件
            Long createUserId = iUserFileService.getUserByFileId(t.getId());
            Long depHeadId = upstageService.selectDepHeadIdByUser(createUserId).getData();
            if (userId.equals(createUserId) || userId.equals(depHeadId)) {//todo 此处应该优化
                iUserFileService.delete(t.getId(), userId);
                String fileName = iUserFileService.getFileNameById(t.getId());
                iUserFileService.deleteLog(t.getId(), fileName, userId);
            } else {
                if(tempFileStr.equals("")){
                    tempFileStr += t.getName();
                }else{
                    tempFileStr += ","+t.getName();
                }
            }
        }
        if(tempFileStr.equals("")){
            return CommonResult.success();
        }else{
            return CommonResult.failed("下列文件不具备删除权限："+tempFileStr);
        }
    }

    @ApiOperation("批量删除文件信息 --审核页面")
    @RequestMapping(value = "/file/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestParam Long[] multipleSelectionId,@RequestParam String reason) {
        List<Long> idList = Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.deleteBatch(idList);
        iUserFileService.updateFilesReason(idList,reason);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "删除文件--审核页面")
    @RequestMapping(value = "/file/delete/fileById", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFile(@RequestParam("id") Long id, @RequestParam String reason) {
        int count = iUserFileService.deleteFile(id);
        iUserFileService.updateFileReason(id,reason);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量删除文件 --回收站页面")
    @RequestMapping(value = "/file/deleteRecycle/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRecycleBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList = Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.deleteBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "删除文件--回收站页面")
    @RequestMapping(value = "/file/deleteRecycle/fileById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteRecycleFileById(@PathVariable("id") Long id) {
        int count = iUserFileService.deleteFile(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量审核文件信息")
    @RequestMapping(value = "/file/pass/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult passBatch(@RequestBody Long[] multipleSelectionId) throws ParseException {
        Object loginUser = getLoginUser();
        List<Long> idList = Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.passBatch(idList, loginUser);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation(value = "审核")
    @RequestMapping(value = "/file/pass/fileById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult passFile(@PathVariable("id") Long id) throws ParseException {
        Object loginUser = getLoginUser();
        int count = iUserFileService.passFile(id, loginUser);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量恢复文件")
    @RequestMapping(value = "/file/recovery/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult recoveryBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList = Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.recoveryBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "恢复文件 回收站")
    @RequestMapping(value = "/file/recovery/fileById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult recoveryFile(@PathVariable("id") Long id) {
        int count = iUserFileService.recoveryFile(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(
            value = "上传单文件",
            notes = "该接口提供了上传单文件的功能"
    )
    @PostMapping("file/upload")
    public CommonResult upload(@Validated FileUploadPO fileUploadPO) throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(getLoginUser());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        iUserFileService.upload(fileUploadPO.getFile(), fileUploadPO.getParentId(), userDepDto, fileUploadPO.getIdentifier(), fileUploadPO.getTotalSize(), fileUploadPO.getFilename());
        return CommonResult.success();
    }

    @ApiOperation(
            value = "分片上传并检查已上传的分片",
            notes = "该接口提供了分片上传并检查已上传的分片的功能"
    )
    @GetMapping("file/upload/chunk-upload")
    public CommonResult<CheckFileChunkUploadVO> checkUploadWithChunk(@Validated FileChunkCheckPO fileChunkCheckPO) throws IOException, ParseException {
        CheckFileChunkUploadVO checkFileChunkUploadVO = iUserFileService.checkUploadWithChunk(getLoginUserId(), fileChunkCheckPO.getIdentifier());
        return CommonResult.success(checkFileChunkUploadVO);
    }

    @ApiOperation(
            value = "分片上传文件",
            notes = "该接口提供了分片上传文件的功能"
    )
    @PostMapping("file/upload/chunk-upload")
    public CommonResult<FileChunkUploadVO> uploadWithChunk(@Validated FileChunkUploadPO fileChunkUploadPO) throws ParseException {
        FileChunkUploadVO fileChunkUploadVO = iUserFileService.uploadWithChunk(fileChunkUploadPO.getFile(), getLoginUserId(), fileChunkUploadPO.getIdentifier(), fileChunkUploadPO.getTotalChunks(), fileChunkUploadPO.getChunkNumber(), fileChunkUploadPO.getTotalSize(), fileChunkUploadPO.getFilename());
        return CommonResult.success(fileChunkUploadVO);
    }

    @ApiOperation(
            value = "合并文件分片",
            notes = "该接口提供了合并文件分片的功能"
    )
    @PostMapping("file/upload/merge")
    public CommonResult mergeChunks(@Validated @RequestBody FileChunkMergePO fileChunkMergePO) throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(getLoginUser());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        Long leaderId = upstageService.selectDepHeadIdByUser(userDepDto.getUserId()).getData();
        if (!leaderId.equals(userDepDto.getUserId())) {
            Message message = new Message();
            message.setWhoName(userDepDto.getNickName());
            message.setFileName(fileChunkMergePO.getFilename());
            message.setMessageType(3);
            message.setCreateDate(new Date());
            message.setReminderId(leaderId);
            message.setReadFlag(0);
            messageService.insert(message);
        }
        iUserFileService.mergeChunks(fileChunkMergePO.getPageType(), fileChunkMergePO.getFilename(), fileChunkMergePO.getIdentifier(), fileChunkMergePO.getParentId(), fileChunkMergePO.getTotalSize(), fileChunkMergePO.getWaterMarkFlag(),fileChunkMergePO.getTeamFlag(),userDepDto);
        return CommonResult.success();
    }

    @ApiOperation(
            value = "秒传文件",
            notes = "该接口提供了秒传文件的功能，在文件生成唯一标识之后上传，根据返回结果决定是否要执行物理上传"
    )
    @PostMapping("file/upload/sec-upload")
    public CommonResult secUpload(@Validated @RequestBody FileSecUploadPO fileSecUploadPO) throws ParseException {
        Object loginUser = getLoginUser();
        if (iUserFileService.secUpload(fileSecUploadPO.getPageType(), fileSecUploadPO.getParentId(), fileSecUploadPO.getFilename(), fileSecUploadPO.getIdentifier(), loginUser)) {
            return CommonResult.success();
        }
        return CommonResult.success("文件唯一标识不存在，请执行物理上传");
    }

    @ApiOperation(
            value = "下载文件(只支持单个下载)",
            notes = "该接口提供了下载文件(只支持单个下载)的功能"
    )
    @GetMapping("file/download")
    public void download(@NotNull(message = "请选择要下载的文件") @RequestParam(value = "fileId", required = false) Long fileId,
                         @RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam(value = "waterMark", required = false) String waterMark,
                         @RequestParam(value = "pagesToWatermark", required = false) Integer[] pagesToWatermark,
                         HttpServletResponse response) {
        List<Integer> WMPages=pagesToWatermark.length>0?Arrays.asList(pagesToWatermark):Arrays.asList();
        iUserFileService.download(fileId,waterMark,WMPages,response);
        String fileName = iUserFileService.getFileNameById(fileId);
        iUserFileService.downloadLog(fileId,fileName,userId, waterMark);
    }

    @ApiOperation("获取前十分享用户数量信息")
    @RequestMapping(value = "file/required/getUserUploadCount", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UserUploadCountDto>> getUserUploadCount() {
        return CommonResult.success(fileOperateService.getUserUploadCount());
    }

    private Object getLoginUser() throws ParseException {
        return upstageService.getCurrentAdmin().getData();
    }

    private Long getLoginDepId() throws ParseException {
        CommonResult restResult = upstageService.getCurrentAdmin();
        String jsonStr = JSONUtil.toJsonStr(restResult.getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        return userDepDto.getDepId();
    }

    private Long getLoginUserId() throws ParseException {
        CommonResult restResult = upstageService.getCurrentAdmin();
        String jsonStr = JSONUtil.toJsonStr(restResult.getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        return userDepDto.getUserId();
    }


    @ApiOperation(
            value = "获取文件夹树",
            notes = "该接口提供了获取文件夹树的功能"
    )
    @GetMapping("file/folder/tree")
    public CommonResult<List<FolderTreeNodeVO>> getFolderTree(@RequestParam("fileRootId") Long fileRootId) throws ParseException {
        return CommonResult.success(iUserFileService.getFolderTree(fileRootId, getLoginUser()));
    }

    @ApiOperation(
            value = "获取小组成员",
            notes = "该接口获取小组成员的功能"
    )
    @GetMapping("file/required/getTeamUser")
    public CommonResult<List<UmsAdminConcat>> getTeamUser(@RequestParam("folderId") Long folderId){
        return CommonResult.success(iUserFileService.getTeamUser(folderId));
    }

    @ApiOperation(value = "修改小组成员")
    @RequestMapping(value = "file/required/updateTeamUser", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult updateTeamUser(@RequestParam(defaultValue = "") String[] teamUsers,@RequestParam("folderId") Long folderId) throws ParseException {


        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        FileUserAuth userFileAuth = iUserFileService.getUserFileAuth(folderId, userDepDto.getUserId());
        if(userFileAuth!=null&&userFileAuth.getFileManageFlag().equals(0)){
            return CommonResult.failed("你没有修改人员信息的权限，请联系管理员");
        }
        //小组创建人本人才可以添加修改人员信息
        Long createUserId = iUserFileService.getUserByFileId(folderId);
        if (userFileAuth==null&&userDepDto.getUserId().equals(createUserId)||userFileAuth.getFileManageFlag().equals(1)) {
            int count = iUserFileService.updateTeamUser(teamUsers, folderId);
            if(count>0){
                return CommonResult.success();
            }
            return CommonResult.failed();
        } else {
            return CommonResult.failed("小组创建人或管理员才可修改人员信息");
        }
    }

    @ApiOperation(
            value = "转移文件(批量)",
            notes = "该接口提供了转移文件(批量)的功能"
    )
    @PostMapping("file/transfer")
    public CommonResult transfer(@Validated @RequestBody TransferPO transferPO) throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(getLoginUser());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        FileUserAuth userFileAuth = iUserFileService.getUserFileAuth(transferPO.getTopFileId(), userDepDto.getUserId());
        if(userFileAuth!=null&&userFileAuth.getMoveFileFlag().equals(0)){
            return CommonResult.failed("你没有移动文件的权限，请联系管理员");
        }
        iUserFileService.transfer(transferPO.getFileIds(), transferPO.getTargetParentId(), getLoginDepId());
        return CommonResult.success();
    }

    @ApiOperation(
            value = "复制文件(批量)",
            notes = "该接口提供了复制文件(批量)的功能"
    )
    @PostMapping("file/copy")
    public CommonResult copy(@Validated @RequestBody CopyPO copyPO) throws ParseException {
        iUserFileService.copy(copyPO.getPageType(), copyPO.getFileIds(), copyPO.getTargetParentId(), getLoginUser());
        return CommonResult.success();
    }

    @ApiOperation(
            value = "获取用户File根信息",
            notes = "该接口提供了获取用户File根信息的功能"
    )
    @GetMapping("file/required/getUserTopFileInfo")
    public CommonResult getUserTopFileInfo(@RequestParam Long depId) {
        return CommonResult.success(rPanUserFileMapper.selectTopFolderByUserId(depId));
    }

    @ApiOperation(
            value = "提供首页展示文件类型数量",
            notes = "该接口提供首页展示文件类型数量的功能"
    )
    @GetMapping("file/required/getTheNumberOfFileTypes")
    public CommonResult getTheNumberOfFileTypes() {
        return CommonResult.success(iUserFileService.getTheNumberOfFileTypes());
    }

    @ApiOperation(
            value = "提供获取团队公告的功能",
            notes = "该接口获取团队公告的功能"
    )
    @GetMapping("file/required/getNotice")
    public CommonResult getNotice(Long teamId) {
        return CommonResult.success(iUserFileService.getNotice(teamId));
    }

    @ApiOperation(
            value = "提供编辑团队公告的功能",
            notes = "该接口编辑团队公告的功能"
    )
    @PostMapping("file/required/editNotice")
    public CommonResult editNotice(TeamUser teamUser) {
        return CommonResult.success(iUserFileService.editNotice(teamUser));
    }


    @ApiOperation(
            value = "提供获取团队人员权限的功能",
            notes = "该接口获取团队人员权限的功能"
    )
    @GetMapping("file/required/getUserFileAuth")
    public CommonResult getUserFileAuth(Long fileId,Long userId) {
        return CommonResult.success(iUserFileService.getUserFileAuth(fileId,userId));
    }

    @ApiOperation(
            value = "提供编辑团队人员权限的功能",
            notes = "该接口编辑团队人员权限的功能"
    )
    @PostMapping("file/required/doUserFileAuth")
    public CommonResult doUserFileAuth(@RequestBody FileUserAuth userFileAuth) throws ParseException {
        Long userId = getLoginUserId();
        FileUserAuth userFileAuth1 = iUserFileService.getUserFileAuth(userFileAuth.getFileId(), userId);
            Long createUserId = iUserFileService.getUserByFileId(userFileAuth.getFileId());
            Long depHeadId = upstageService.selectDepHeadIdByUser(createUserId).getData();
            if ((userFileAuth1==null&&(userId.equals(createUserId) || userId.equals(depHeadId)))||(userFileAuth1!=null&&userFileAuth1.getFileManageFlag().equals(1))) {
                return CommonResult.success(iUserFileService.doUserFileAuth(userFileAuth));
            }else {
                return CommonResult.failed("管理员或者创建人本人及其部门长,才可进行权限设置");
            }
    }

    /**
     * 通过文件名搜索文件列表
     *
     * @param keyword
     * @param fileTypes
     * @return
     */
    @ApiOperation(
            value = "通过文件名搜索文件列表",
            notes = "该接口提供了通过文件名搜索文件列表的功能"
    )
    @GetMapping("file/search")
    public CommonResult<List<RPanUserFileSearchVO>> search(@NotBlank(message = "关键字不能为空") @RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes) throws ParseException {
        return CommonResult.success(iUserFileService.search(keyword, fileTypes, getLoginUserId()));
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @return
     */
    @ApiOperation(
            value = "查询文件详情",
            notes = "该接口提供了查询文件详情的功能"
    )
    @GetMapping("file")
    public CommonResult<RPanUserFileDisplayVO> detail(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId) throws ParseException {
        return CommonResult.success(iUserFileService.detail(fileId, getLoginUserId()));
    }

    /**
     * 获取面包屑列表
     *
     * @return
     */
    @ApiOperation(
            value = "获取面包屑列表",
            notes = "该接口提供了获取面包屑列表的功能"
    )
    @GetMapping("file/breadcrumbs")
    public CommonResult<List<BreadcrumbVO>> getBreadcrumbs(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId) throws ParseException {
        return CommonResult.success(iUserFileService.getBreadcrumbs(fileId, getLoginUser()));
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @return
     */
    @ApiOperation(
            value = "预览单个文件",
            notes = "该接口提供了预览单个文件的功能"
    )
    @GetMapping("preview")
    public void preview(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId,
                        @RequestParam(value = "userName", required = false) String userName,
                        HttpServletResponse response) {
        iUserFileService.preview(fileId, userName, response);
    }

    @ApiOperation(
            value = "预览单个文件",
            notes = "该接口提供了预览单个文件的功能"
    )
    @GetMapping("previewByFilePath")
    public void previewByFilePath(@RequestParam(value = "filePath") String filePath,
                                  HttpServletResponse response,
                                  @RequestParam(value = "filePreviewContentType") String filePreviewContentType) {
        iUserFileService.preview(filePath, "", response, filePreviewContentType);
    }

    @GetMapping("image/{fileName}")
    public void getCommentImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        //1.file源文件
        ServletOutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream("d:\\Users\\naccl\\Desktop\\upload\\" + fileName);
        response.setContentType("image/png");//告诉浏览器显示图片
        //response.setContentType("multipart/form-data");//告诉浏览器下载图片
        out = response.getOutputStream();
        //读取文件流
        int len = 0;
        byte[] buffer = new byte[1024 * 10];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
    }

    @ApiOperation(
            value = "迁移文件(从部门到企业)",
            notes = "该接口提供了迁移文件(从部门到企业)的功能"
    )
    @GetMapping("/depToEnterprise")
    public CommonResult depToEnterprise(@NotNull(message = "请选择要转移的文件") @RequestParam(value = "fileId", required = false) Long fileId,
                                @RequestParam(value = "userId", required = false) Long userId,
                                @RequestParam(value = "targetParentId", required = false) Long targetParentId) {
        iUserFileService.depToEnterprise(fileId,userId,targetParentId);
        return CommonResult.success();
        //String fileName = iUserFileService.getFileNameById(fileId);
       // iUserFileService.downloadLog(fileId,fileName,userId, "");   //记录迁移文件操作
    }
}
