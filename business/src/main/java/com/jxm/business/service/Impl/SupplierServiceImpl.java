package com.jxm.business.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.business.mapper.SupplierMapper;
import com.jxm.business.model.SupplierParam;
import com.jxm.business.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

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
}
