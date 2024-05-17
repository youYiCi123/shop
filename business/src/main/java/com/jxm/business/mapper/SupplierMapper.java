package com.jxm.business.mapper;

import com.jxm.business.model.SupplierParam;

import java.util.List;

public interface SupplierMapper {

    List<SupplierParam> getSupplierBySearch(String keyword,String businessAutStartDate,String businessAutEndDate);

    SupplierParam querySupplierContent(Long id);

    int addSupplier(SupplierParam supplierParam);

    int updateSupplier(SupplierParam supplierParam);

    int delete(Long supplierId);

    int deleteBatchSupplier(List<Long> idList);

    List<String> getAllSupplier();

    int saveBatch(List<SupplierParam> supplierParams);

}
