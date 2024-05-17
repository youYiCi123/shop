package com.jxm.business.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.dto.SupplierBriefDto;
import com.jxm.business.mapper.RemindMapper;
import com.jxm.business.mapper.SupplierMapper;
import com.jxm.business.model.RemindParam;
import com.jxm.business.model.SupplierParam;
import com.jxm.business.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private RemindMapper remindMapper;

    @Override
    public List<SupplierParam> getSupplierBySearch(Integer pageNum, Integer pageSize, String keyword,String[] businessAuthTime) {
        String businessAutStartDate = null;
        String businessAutEndDate = null;
        if (businessAuthTime.length == 2) {
            businessAutStartDate = businessAuthTime[0];
            businessAutEndDate = businessAuthTime[1];
        }
        PageHelper.startPage(pageNum, pageSize);
        return supplierMapper.getSupplierBySearch(keyword,businessAutStartDate,businessAutEndDate);
    }

    @Override
    public SupplierParam querySupplierContent(Long id) {
        return supplierMapper.querySupplierContent(id);
    }

    @Override
    public int addSupplier(SupplierParam supplierParam) {
        return supplierMapper.addSupplier(supplierParam);
    }

    @Override
    public int updateSupplier(SupplierParam supplierParam) {
        return supplierMapper.updateSupplier(supplierParam);
    }

    @Override
    public int delete(Long supplierId) {
        return supplierMapper.delete(supplierId);
    }

    @Override
    public int deleteBatchSupplier(List<Long> idList) {
        return supplierMapper.deleteBatchSupplier(idList);
    }

    @Override
    public List<String> getAllSupplier() {
        return supplierMapper.getAllSupplier();
    }

    @Override
    public int saveSupplierBatch(List<SupplierParam> supplierParams) {
        return supplierMapper.saveBatch(supplierParams);
    }

    @Override
    public RemindParam getRemind() {
        return remindMapper.querySupplier();
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
    public List<SupplierBriefDto> getSupplierByNearDeadline(int days) {
        return remindMapper.getSupplierByNearDeadline(days);
    }

}
