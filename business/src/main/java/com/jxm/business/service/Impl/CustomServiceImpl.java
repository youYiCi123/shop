package com.jxm.business.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.dto.UmsAdminConcat;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.CustomMapper;
import com.jxm.business.mapper.CustomSaleMapper;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;
import com.jxm.business.model.CustomSaleParam;
import com.jxm.business.service.CustomService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private CustomSaleMapper customSaleMapper;

    @Override
    public List<CustomParam> getCustomBySearch(Integer pageNum, Integer pageSize, String keyword,Long salesPersonId) {
        PageHelper.startPage(pageNum, pageSize);
        return customMapper.getCustomBySearch(keyword,salesPersonId);
    }

    @Override
    public List<CustomParam> getCustomByNearDeadline() {
        return customMapper.getCustomByNearDeadline();
    }

    @Override
    public CustomParam queryCustomContent(Long id) {
        return customMapper.queryCustomContent(id);
    }

    @Override
    public int addCustom(CustomSalesParam customSalesParam) {
        CustomPostParam customPostParam = new CustomPostParam();
        UniqueIdGenerator uniqueId = new UniqueIdGenerator(1,1);
        long customId = uniqueId.nextId();
        customPostParam.setId(customId);
        customPostParam.setCustomName(customSalesParam.getCustomName());
        customPostParam.setCreditCode(customSalesParam.getCreditCode());
        customPostParam.setAddress(customSalesParam.getAddress());
        customPostParam.setLegalPerson(customSalesParam.getLegalPerson());
        customPostParam.setLicenseTime(customSalesParam.getLicenseTime());
        customPostParam.setContactName(customSalesParam.getContactName());
        customPostParam.setContactPhone(customSalesParam.getContactPhone());
        customPostParam.setBusinessTimeType(customSalesParam.getBusinessTimeType());
        if(customSalesParam.getBusinessTimeType()==0){
            customPostParam.setBusinessTime(null);
        }else{
            customPostParam.setBusinessTime(customSalesParam.getBusinessTime());
        }
        int count = customMapper.addCustom(customPostParam);
        if(count>0){
            UmsAdminConcat umsAdminConcat = upstageService.getUmsAdminConcat(customSalesParam.getSalesPersonId()).getData();
            CustomSaleParam customSaleParam = new CustomSaleParam();
            customSaleParam.setId(uniqueId.nextId());
            customSaleParam.setSalesPersonId(customSalesParam.getSalesPersonId());
            customSaleParam.setSalesPersonName(umsAdminConcat.getNickName());
            customSaleParam.setSalesPersonEmail(umsAdminConcat.getEmail());
            customSaleParam.setSalesPersonPhone(umsAdminConcat.getUsername());
            customSaleParam.setCustomId(customId);
           return customSaleMapper.addCustomSales(customSaleParam);
        }
        return 0;
    }

    @Override
    public int updateCustom(CustomSalesParam customSalesParam) {
        CustomPostParam customPostParam = new CustomPostParam();
        customPostParam.setId(customSalesParam.getId());
        customPostParam.setCustomName(customSalesParam.getCustomName());
        customPostParam.setCreditCode(customSalesParam.getCreditCode());
        customPostParam.setAddress(customSalesParam.getAddress());
        customPostParam.setLegalPerson(customSalesParam.getLegalPerson());
        customPostParam.setLicenseTime(customSalesParam.getLicenseTime());
        customPostParam.setContactName(customSalesParam.getContactName());
        customPostParam.setContactPhone(customSalesParam.getContactPhone());
        customPostParam.setBusinessTimeType(customSalesParam.getBusinessTimeType());
        if(customSalesParam.getBusinessTimeType()==0){
            customPostParam.setBusinessTime(null);
        }else{
            customPostParam.setBusinessTime(customSalesParam.getBusinessTime());
        }
        int count=customMapper.updateCustom(customPostParam);
        if(count>0){
            UmsAdminConcat umsAdminConcat = upstageService.getUmsAdminConcat(customSalesParam.getSalesPersonId()).getData();
            CustomSaleParam customSaleParam = new CustomSaleParam();
            customSaleParam.setSalesPersonId(customSalesParam.getSalesPersonId());
            customSaleParam.setSalesPersonName(umsAdminConcat.getNickName());
            customSaleParam.setSalesPersonEmail(umsAdminConcat.getEmail());
            customSaleParam.setSalesPersonPhone(umsAdminConcat.getUsername());
            customSaleParam.setCustomId(customSalesParam.getId());
            return customSaleMapper.updateCustomSale(customSaleParam);
        }
        return 0;
    }

    @Override
    public int delete(Long customId) {
        int count = customMapper.delete(customId);
        if (count > 0) {
            return customSaleMapper.delete(customId);
        }
        return -1;
    }

    @Override
    public int deleteBatchCustom(List<Long> idList) {
        int count = customMapper.deleteBatchCustom(idList);
        if (count > 0) {
            return customSaleMapper.deleteBatchCustom(idList);
        }
        return -1;
    }

    @Override
    public List<String> getAllCustom() {
        return customMapper.getAllCustom();
    }

    @Override
    public int saveCustomBatch(List<CustomPostParam> customPostParamList) {
        return customMapper.saveBatch(customPostParamList);
    }

    @Override
    public int saveCustomSaleBatch(List<CustomSaleParam> customSaleParams) {
        return customSaleMapper.saveBatch(customSaleParams);
    }
}
