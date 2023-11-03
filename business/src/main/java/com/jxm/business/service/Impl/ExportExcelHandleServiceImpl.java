package com.jxm.business.service.Impl;

import cn.hutool.crypto.digest.BCrypt;
import com.jxm.business.dto.ExcelCustom;
import com.jxm.business.dto.UmsAdminConcat;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.model.CustomPostParam;
import com.jxm.business.model.CustomSaleParam;
import com.jxm.business.service.CustomService;
import com.jxm.business.service.ExportExcelHandleService;
import com.jxm.common.generator.UniqueIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ExportExcelHandleServiceImpl implements ExportExcelHandleService {

    @Autowired
    private CustomService customService;

    @Autowired
    private UpstageService upstageService;

    @Override
    public void saveCustomInfo(List<ExcelCustom> userExportExcelModels) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //总条件、失败条数
        Integer totalCount = userExportExcelModels.size(), successCount = 0, failCount = 0;

        List<CustomPostParam> addCustomInfoList = new ArrayList<>();
        List<CustomSaleParam> addCustomSaleList = new ArrayList<>();
        try {
            //校验excel数据是否重复
            long samePhoneCount = userExportExcelModels.stream().map(ExcelCustom::getCustomName).filter(Objects::nonNull).distinct().count();
            if (samePhoneCount != totalCount) {
                failCount = totalCount;
                request.getSession().setAttribute("success", 0);
                request.getSession().setAttribute("fail", failCount);
                return;
            }

            List<UmsAdminConcat> umsAdminConcatList = upstageService.getUmsAdminConcatList().getData();

            //判断手机号、部门列表是否已经存在
            List<String> allCustom = customService.getAllCustom();
            UniqueIdGenerator idGenerator = new UniqueIdGenerator(1, 1);
            boolean addFlag = false;
            for (ExcelCustom customExportExcelModel : userExportExcelModels) {
                if (allCustom.size() == 0) {
                    addFlag = true;
                } else {
                    Optional<String> subOptional = allCustom.stream().filter(Objects::nonNull).filter(t -> t.equals(customExportExcelModel.getCustomName())).findFirst();
                    if (!subOptional.isPresent()) {
                        addFlag = true;
                    }
                }
                if (addFlag) {
                    CustomPostParam customPostParam = new CustomPostParam();
                    long customId = idGenerator.nextId();
                    customPostParam.setId(customId);
                    customPostParam.setCustomName(customExportExcelModel.getCustomName());
                    customPostParam.setCreditCode(customExportExcelModel.getCreditCode());
                    customPostParam.setContactPhone(customExportExcelModel.getContactPhone());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date licenseTime = sdf.parse(customExportExcelModel.getLicenseTime());
                    customPostParam.setLicenseTime(licenseTime);
                    if (customExportExcelModel.getBusinessTime().equals("长期")) {
                        customPostParam.setBusinessTime(null);
                        customPostParam.setBusinessTimeType(0);
                    } else {
                        Date businessTime = sdf.parse(customExportExcelModel.getBusinessTime());
                        customPostParam.setBusinessTime(businessTime);
                        customPostParam.setBusinessTimeType(1);
                    }
                    customPostParam.setContactName(customExportExcelModel.getContactName());
                    customPostParam.setAddress(customExportExcelModel.getAddress());
                    customPostParam.setLegalPerson(customExportExcelModel.getLegalPerson());
                    addCustomInfoList.add(customPostParam);

                    CustomSaleParam customSaleParam = new CustomSaleParam();
                    customSaleParam.setId(idGenerator.nextId());
                    customSaleParam.setCustomId(customId);
                    Optional<UmsAdminConcat> first = umsAdminConcatList.stream().filter(t -> t.getNickName().equals(customExportExcelModel.getSalesPersonName())).findFirst();
                    if (first.isPresent()) {
                        customSaleParam.setSalesPersonId(first.get().getId());
                        customSaleParam.setSalesPersonEmail(first.get().getEmail());
                        customSaleParam.setSalesPersonPhone(first.get().getUsername());
                    }
                    customSaleParam.setSalesPersonName(customExportExcelModel.getSalesPersonName());

                    addCustomSaleList.add(customSaleParam);
                    successCount++;
                }
            }
            if (addCustomInfoList.size() != 0)
            customService.saveCustomBatch(addCustomInfoList);
            customService.saveCustomSaleBatch(addCustomSaleList);
            failCount = totalCount - successCount;
            request.getSession().setAttribute("success", successCount);
            request.getSession().setAttribute("fail", failCount);
        } catch (Exception e) {
            log.error("保存人员数据失败:", e);
        }
    }
}
