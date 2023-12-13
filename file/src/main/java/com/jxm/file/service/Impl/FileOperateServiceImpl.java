package com.jxm.file.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.file.dto.FileOperateLogDetail;
import com.jxm.file.dto.UmsAdminConcat;
import com.jxm.file.dto.UserUploadCountDto;
import com.jxm.file.dto.userIdUploadDto;
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
    public List<FileOperateLogDetail> getFileOperateLog(String startDate,String endDate,Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileOperateLog> fileOperateLogs=fileOperateLogMapper.selectFileOperateLog(startDate,endDate,userId);
        List<FileOperateLogDetail> fileOperateLogDetails=new ArrayList<>();
        fileOperateLogs.stream().forEach(t->{
            FileOperateLogDetail fileOperateLogDetail = new FileOperateLogDetail();
            fileOperateLogDetail.setCreateTime(t.getCreateTime());
            fileOperateLogDetail.setFileId(t.getFileId());
            fileOperateLogDetail.setId(t.getId());
            fileOperateLogDetail.setOperate(t.getOperate());
            fileOperateLogDetail.setUserId(t.getUserId());
            UmsAdminConcat adminConcat = upstageService.getUmsAdminConcat(t.getUserId()).getData();
            fileOperateLogDetail.setUserName(adminConcat.getNickName());
            RPanUserFile rPanFile = rPanUserFileMapper.selectByPrimaryKey(t.getFileId());
            fileOperateLogDetail.setFileName(rPanFile.getFilename());
            fileOperateLogDetails.add(fileOperateLogDetail);
        });
        return fileOperateLogDetails;
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
