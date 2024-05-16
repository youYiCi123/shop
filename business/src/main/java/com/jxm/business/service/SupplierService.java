package com.jxm.business.service;

import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.SupplierParam;

import java.util.List;

public interface SupplierService {

    List<SupplierParam> getSupplierBySearch(Integer pageNum, Integer pageSize, String keyword,String[] businessAuthTime);

    SupplierParam querySupplierContent(Long id);

    int addSupplier(SupplierParam supplierParam);

    int updateSupplier(SupplierParam supplierParam);

    int delete(Long supplierId);

    int deleteBatchSupplier(List<Long> idList);
}
