package com.jxm.upstage.service.Impl;

import cn.hutool.crypto.digest.BCrypt;
import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.upstage.dto.DepIdToName;
import com.jxm.upstage.dto.ExcelUser;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.service.DepService;
import com.jxm.upstage.service.ExportExcelHandleService;
import com.jxm.upstage.service.UmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
public class ExportExcelHandleServiceImpl implements ExportExcelHandleService {

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private DepService depService;
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    @Override
    public void saveUserInfo(List<ExcelUser> userExportExcelModels) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //总条件、失败条数
        Integer totalCount = userExportExcelModels.size(), successCount = 0, failCount = 0;

        List<UmsAdmin> addUserInfoList = new ArrayList<>();

        try {
            //校验excel数据是否重复
            long samePhoneCount = userExportExcelModels.stream().map(ExcelUser::getPhoneNumber).filter(Objects::nonNull).distinct().count();
            if (samePhoneCount != totalCount) {
                failCount = totalCount;
                request.getSession().setAttribute("success", 0);
                request.getSession().setAttribute("fail", failCount);
                return;
            }

            //判断手机号、部门列表是否已经存在
            List<String> allUserPhone = umsAdminService.getAllUserPhone();
            List<DepIdToName> depIdToNames = depService.getDepIdToName();

            boolean addFlag = false;
            for (ExcelUser userExportExcelModel : userExportExcelModels) {

//                if(!Pattern.matches(REGEX_MOBILE, userExportExcelModel.getPhoneNumber()))
//                    continue;

                if (allUserPhone.size() == 0) {
                    addFlag = true;
                } else {
                    Optional<String> subOptional = allUserPhone.stream().filter(Objects::nonNull).filter(t -> t.equals(userExportExcelModel.getPhoneNumber())).findFirst();
                    if (!subOptional.isPresent()) {
                        addFlag = true;
                    }
                }

                if (addFlag) {
                    UmsAdmin userInfo = new UmsAdmin();
                    UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
                    userInfo.setId(idGenerator.nextId());
                    userInfo.setUsername(userExportExcelModel.getPhoneNumber());
                    userInfo.setPhone(userExportExcelModel.getPhoneNumber());
                    userInfo.setEmail(userExportExcelModel.getEmail());
                    userInfo.setNickName(userExportExcelModel.getNickName());
                    if(userExportExcelModel.getSex().equals("男")){
                        userInfo.setSex(1);
                    }else{
                        userInfo.setSex(0);
                    }
                    userInfo.setStatus(1);
                    userInfo.setCreateTime(new Date());
                    userInfo.setPassword(BCrypt.hashpw("123456"));
                    //部门名转换成id存储
                    Optional<DepIdToName> depIdToName = depIdToNames.stream().filter(t -> t.getDepName().equals(userExportExcelModel.getDepName())).findFirst();
                    depIdToName.ifPresent(one->userInfo.setDepId(one.getId()));
                    userInfo.setDuty(userExportExcelModel.getDuty());
                    addUserInfoList.add(userInfo);
                    successCount++;
                }
            }
            if(addUserInfoList.size()!=0)
            umsAdminService.saveBatch(addUserInfoList);

            failCount = totalCount - successCount;
            request.getSession().setAttribute("success", successCount);
            request.getSession().setAttribute("fail", failCount);
        } catch (Exception e) {
            log.error("保存人员数据失败:", e);
        }
    }
}
