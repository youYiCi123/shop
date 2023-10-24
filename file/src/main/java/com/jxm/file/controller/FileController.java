package com.jxm.file.controller;

import cn.hutool.json.JSONUtil;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.common.service.RedisService;
import com.jxm.file.dto.UserDepDto;
import com.jxm.file.entity.RPanUserFile;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.mapper.RPanUserFileMapper;
import com.jxm.file.po.*;
import com.jxm.file.service.IUserFileService;
import com.jxm.file.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    @Value("${spring.redis.key.admin}")
    private String REDIS_KEY_ADMIN;


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
        List<RPanUserFileDisplayVO> list = iUserFileService.filesForTable(pageType,keyword,fileType, pageNum, pageSize, getLoginDepId());
        return CommonResult.success(CommonPage.restPage(list));
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    @ResponseBody
        public CommonResult<List<RPanUserFileDisplayVO>> list(@RequestParam(value = "pageType", required = false) Long pageType,
                                                          @RequestParam(value = "parentId", required = false) Long parentId,
                                                          @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes
    ) throws ParseException {
        List<RPanUserFileDisplayVO> list = iUserFileService.list(pageType, parentId, fileTypes, getLoginDepId());
        return CommonResult.success(list);
    }

    @ApiOperation(
            value = "新建文件夹",
            notes = "该接口提供了新建文件夹的功能"
    )
    @PostMapping("file/folder")
    public CommonResult createFolder(@Validated @RequestBody CreateFolderPO createFolderPO) throws ParseException {
        iUserFileService.createFolder(createFolderPO.isPageType(), createFolderPO.getParentId(), createFolderPO.getFolderName(), getLoginUser());
        return CommonResult.success();
    }

    @PostMapping("file/createDepRootFolder")
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
        iUserFileService.saveSet(basicSetFilePO.getFileId(), basicSetFilePO.getIsWaterMater(), loginUserId);
        return CommonResult.success();
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
            value = "删除文件(批量)",
            notes = "该接口提供了删除文件(批量)的功能"
    )
    @PostMapping("/file/delete")
    public CommonResult delete(@Validated @RequestBody DeletePO deletePO){
        iUserFileService.delete(deletePO.getFileIds());
        return CommonResult.success();
    }

    @ApiOperation("批量删除文件信息")
    @RequestMapping(value = "/file/deleteBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.deleteBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "删除文件")
    @RequestMapping(value = "/file/deleteFile/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteFile(@PathVariable("id") Long id) {
        int count = iUserFileService.deleteFile(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量审核文件信息")
    @RequestMapping(value = "/file/passeBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult passeBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = iUserFileService.passeBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "审核")
    @RequestMapping(value = "/file/passFile/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult passFile(@PathVariable("id") Long id) throws ParseException {
        Object loginUser = getLoginUser();
        int count = iUserFileService.passFile(id,loginUser);
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
        Object loginUser = getLoginUser();
        iUserFileService.upload(fileUploadPO.getFile(), fileUploadPO.getParentId(), loginUser, fileUploadPO.getIdentifier(), fileUploadPO.getTotalSize(), fileUploadPO.getFilename());
        return CommonResult.success();
    }

    @ApiOperation(
            value = "分片上传并检查已上传的分片",
            notes = "该接口提供了分片上传并检查已上传的分片的功能"
    )
    @GetMapping("file/chunk-upload")
    public CommonResult<CheckFileChunkUploadVO> checkUploadWithChunk(@Validated FileChunkCheckPO fileChunkCheckPO) throws IOException, ParseException {
        CheckFileChunkUploadVO checkFileChunkUploadVO = iUserFileService.checkUploadWithChunk(getLoginUserId(), fileChunkCheckPO.getIdentifier());
        return CommonResult.success(checkFileChunkUploadVO);
    }

    @ApiOperation(
            value = "分片上传文件",
            notes = "该接口提供了分片上传文件的功能"
    )
    @PostMapping("file/chunk-upload")
    public CommonResult<FileChunkUploadVO> uploadWithChunk(@Validated FileChunkUploadPO fileChunkUploadPO) throws ParseException {
        FileChunkUploadVO fileChunkUploadVO = iUserFileService.uploadWithChunk(fileChunkUploadPO.getFile(), getLoginUserId(), fileChunkUploadPO.getIdentifier(), fileChunkUploadPO.getTotalChunks(), fileChunkUploadPO.getChunkNumber(), fileChunkUploadPO.getTotalSize(), fileChunkUploadPO.getFilename());
        return CommonResult.success(fileChunkUploadVO);
    }

    @ApiOperation(
            value = "合并文件分片",
            notes = "该接口提供了合并文件分片的功能"
    )
    @PostMapping("file/merge")
    public CommonResult mergeChunks(@Validated @RequestBody FileChunkMergePO fileChunkMergePO) throws ParseException {
        Object loginUser = getLoginUser();
        iUserFileService.mergeChunks(fileChunkMergePO.getPageType(),fileChunkMergePO.getFilename(), fileChunkMergePO.getIdentifier(), fileChunkMergePO.getParentId(), fileChunkMergePO.getTotalSize(), loginUser);
        return CommonResult.success();
    }

    @ApiOperation(
            value = "秒传文件",
            notes = "该接口提供了秒传文件的功能，在文件生成唯一标识之后上传，根据返回结果决定是否要执行物理上传"
    )
    @PostMapping("file/sec-upload")
    public CommonResult secUpload(@Validated @RequestBody FileSecUploadPO fileSecUploadPO) throws ParseException {
        Object loginUser = getLoginUser();
        if (iUserFileService.secUpload(fileSecUploadPO.getPageType(),fileSecUploadPO.getParentId(), fileSecUploadPO.getFilename(), fileSecUploadPO.getIdentifier(), loginUser)) {
            return CommonResult.success();
        }
        return CommonResult.failed("文件唯一标识不存在，请执行物理上传");
    }

    @ApiOperation(
            value = "下载文件(只支持单个下载)",
            notes = "该接口提供了下载文件(只支持单个下载)的功能"
    )
    @GetMapping("file/download")
    public void download(@NotNull(message = "请选择要下载的文件") @RequestParam(value = "fileId", required = false) Long fileId,
                         @RequestParam(value = "waterMark", required = false) String waterMark,
                         HttpServletResponse response) {
        iUserFileService.download(fileId,waterMark,response);
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
        return CommonResult.success(iUserFileService.getFolderTree(fileRootId, getLoginDepId()));
    }


    @ApiOperation(
            value = "转移文件(批量)",
            notes = "该接口提供了转移文件(批量)的功能"
    )
    @PostMapping("file/transfer")
    public CommonResult transfer(@Validated @RequestBody TransferPO transferPO) throws ParseException {
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
    @GetMapping("file/getUserTopFileInfo")
    public CommonResult getUserTopFileInfo(@RequestParam Long depId) {
        return CommonResult.success(rPanUserFileMapper.selectTopFolderByUserId(depId));
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
        return CommonResult.success(iUserFileService.getBreadcrumbs(fileId, getLoginDepId()));
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
                        HttpServletResponse response){
        iUserFileService.preview(fileId, response);
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

}
