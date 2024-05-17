package com.jxm.business.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.mapper.CertificateMapper;
import com.jxm.business.mapper.RemindMapper;
import com.jxm.business.model.CertificateParam;
import com.jxm.business.model.RemindParam;
import com.jxm.business.service.CertificateService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private RemindMapper remindMapper;

    @Override
    public List<CertificateParam> getListBySearch(String[] lastDeclareTime, String[] firstRegisterTime, String keyword,
                                                  Long category, Integer pageNum, Integer pageSize) {
        String lastDeclareStartDate = null;
        String lastDeclareEndDate = null;
        String firstRegisterStartDate = null;
        String firstRegisterEndDate = null;
        if (lastDeclareTime.length == 2) {
            lastDeclareStartDate = lastDeclareTime[0];
            lastDeclareEndDate = lastDeclareTime[1];
        }
        if (firstRegisterTime.length == 2) {
            firstRegisterStartDate = lastDeclareTime[0];
            firstRegisterEndDate = lastDeclareTime[1];
        }

        PageHelper.startPage(pageNum, pageSize);
        return certificateMapper.getListBySearch(lastDeclareStartDate, lastDeclareEndDate, firstRegisterStartDate, firstRegisterEndDate,
                keyword, category);

    }

    @Override
    public CertificateParam queryContent(Long id) {
        return certificateMapper.queryContent(id);
    }

    @Override
    public RemindParam getRemind() {
        return remindMapper.queryCertificate();
    }

    @Override
    public int setRemind(RemindParam remindParam) {
        if(remindParam.getId().equals(-1)){
            return remindMapper.add(remindParam);
        }else{//修改
            return remindMapper.update(remindParam);
        }
    }

    @Override
    public int add(CertificateParam certificateParam) {
        UniqueIdGenerator uniqueId = new UniqueIdGenerator(1,1);
        long customId = uniqueId.nextId();
        certificateParam.setId(customId);
        return certificateMapper.add(certificateParam);
    }

    @Override
    public int update(CertificateParam certificateParam) {
        return certificateMapper.update(certificateParam);
    }

    @Override
    public int delete(Long id) {
        return certificateMapper.delete(id);
    }

    @Override
    public int deleteBatch(List<Long> idList) {
        return certificateMapper.deleteBatch(idList);
    }

    @Override
    public int saveCertificateBatch(List<CertificateParam> certificateParams) {
        return certificateMapper.saveBatch(certificateParams);
    }

    @Override
    public List<CertificateBriefDto> getCertificateByNearDeadline(int days) {
        return remindMapper.getCertificateByNearDeadline(days);
    }
}
