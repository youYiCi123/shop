package com.jxm.file.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.common.api.CommonPage;
import com.jxm.file.dto.*;
import com.jxm.file.entity.FileOperateLog;
import com.jxm.file.entity.RPanUserFile;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.mapper.FileOperateLogMapper;
import com.jxm.file.mapper.RPanUserFileMapper;
import com.jxm.file.service.FileOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileOperateServiceImpl implements FileOperateService {

    @Autowired
    @Qualifier(value = "rPanUserFileMapper")
    private RPanUserFileMapper rPanUserFileMapper;

    @Autowired
    private UpstageService upstageService;

    @Autowired
    @Qualifier(value = "fileOperateLogMapper")
    private FileOperateLogMapper fileOperateLogMapper;

    @Override
    public List<UserUploadCountDto> getUserUploadCount() {
        List<UserUploadCountDto> userUploadCountDtos=new ArrayList<>();
        List<userIdUploadDto> userUploadCount = fileOperateLogMapper.getUserUploadCount();
        List<UmsAdminConcat> umsAdminConcats = upstageService.getUmsAdminConcatList().getData();
        for (userIdUploadDto uploadDto: userUploadCount) {
            UserUploadCountDto userUploadCountDto = new UserUploadCountDto();
            umsAdminConcats.stream().forEach(t->{
                if(uploadDto.getUserId().equals(t.getId())){
                    userUploadCountDto.setUserName(t.getNickName());
                }
            });
            userUploadCountDto.setUploadCount(uploadDto.getUploadCount());
            userUploadCountDtos.add(userUploadCountDto);
        }
        return userUploadCountDtos;
    }

    @Override
    public CommonPage<FileOperateLogDetail> getFileOperateLog(String startDate,String endDate,Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileOperateLog> fileOperateLogs=fileOperateLogMapper.selectFileOperateLog(startDate,endDate,userId);
        CommonPage<FileOperateLog> fileOperateLogCommonPage = CommonPage.restPage(fileOperateLogs);
        List<FileOperateLogDetail> fileOperateLogDetails=new ArrayList<>();
        fileOperateLogCommonPage.getList().stream().forEach(t->{
            FileOperateLogDetail fileOperateLogDetail = new FileOperateLogDetail();
            fileOperateLogDetail.setCreateTime(t.getCreateTime());
            fileOperateLogDetail.setFileId(t.getFileId());
            fileOperateLogDetail.setId(t.getId());
            fileOperateLogDetail.setOperate(t.getOperate());
            fileOperateLogDetail.setExtendField(t.getExtendField());
            fileOperateLogDetail.setUserId(t.getUserId());
            UmsAdminConcat adminConcat = upstageService.getUmsAdminConcat(t.getUserId()).getData();
            if(adminConcat!=null){
                fileOperateLogDetail.setUserName(adminConcat.getNickName());
            }
            fileOperateLogDetail.setFileName(t.getFileName());
            fileOperateLogDetails.add(fileOperateLogDetail);
        });
        CommonPage<FileOperateLogDetail> fileOperateLogDetailCommonPage=new CommonPage<>();
        fileOperateLogDetailCommonPage.setList(fileOperateLogDetails);
        fileOperateLogDetailCommonPage.setTotal(fileOperateLogCommonPage.getTotal());
        fileOperateLogDetailCommonPage.setPageNum(fileOperateLogCommonPage.getPageNum());
        fileOperateLogDetailCommonPage.setPageSize(fileOperateLogCommonPage.getPageSize());
        fileOperateLogDetailCommonPage.setTotalPage(fileOperateLogCommonPage.getTotalPage());
        return fileOperateLogDetailCommonPage;
    }

    @Override
    public CommonPage<MyUploadFileStatus> getMyUploadFileStatus(String startDate, String endDate, Long userId,String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileOperateLog> fileOperateLogs=fileOperateLogMapper.getMyUploadFileStatus(startDate,endDate,userId,keyword);
        CommonPage<FileOperateLog> fileOperateLogCommonPage = CommonPage.restPage(fileOperateLogs);
        List<MyUploadFileStatus> myUploadFileStatuses=new ArrayList<>();
        fileOperateLogCommonPage.getList().stream().forEach(t->{
            MyUploadFileStatus myUploadFileStatus = new MyUploadFileStatus();
            myUploadFileStatus.setFileId(t.getFileId());
            myUploadFileStatus.setFileName(t.getFileName());
            myUploadFileStatus.setCreateTime(t.getCreateTime());
            RPanUserFile rPanFile = rPanUserFileMapper.selectByPrimaryKey(t.getFileId());
            /**  状态 0 待审核   rPanFile.passFlag==0
             *       1 审核通过  rPanFile.passFlag==1
             *       2 审核未通过/已删除 rPanFile==null
             *       **/
            if(rPanFile!=null){
                if(rPanFile.getPassFlag()==1){
                    myUploadFileStatus.setStatus(1);
                }else{
                    myUploadFileStatus.setStatus(0);
                }
            }else{
                myUploadFileStatus.setReason(t.getReason());
                myUploadFileStatus.setStatus(2);
            }
            myUploadFileStatuses.add(myUploadFileStatus);
        });
        CommonPage<MyUploadFileStatus> myUploadFileStatusCommonPage=new CommonPage<>();
        myUploadFileStatusCommonPage.setList(myUploadFileStatuses);
        myUploadFileStatusCommonPage.setTotal(fileOperateLogCommonPage.getTotal());
        myUploadFileStatusCommonPage.setPageNum(fileOperateLogCommonPage.getPageNum());
        myUploadFileStatusCommonPage.setPageSize(fileOperateLogCommonPage.getPageSize());
        myUploadFileStatusCommonPage.setTotalPage(fileOperateLogCommonPage.getTotalPage());
        return myUploadFileStatusCommonPage;
    }

    @Override
    public int deleteLog(Long logId) {
        return fileOperateLogMapper.deleteLog(logId);
    }

    @Override
    public int deleteBatchLog(List<Long> idList) {
        return fileOperateLogMapper.deleteBatchLog(idList);
    }
}
