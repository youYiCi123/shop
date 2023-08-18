package com.jxm.upstage.service;



import com.jxm.common.api.CommonResult;
import com.jxm.common.domain.UserDto;
import com.jxm.upstage.dto.UmsAdminLoginParam;
import com.jxm.upstage.dto.UmsAdminParam;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.model.UmsRole;

import java.text.ParseException;
import java.util.List;


/**
 * 后台管理员Service
 * Created by macro on 2018/4/26.
 */
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 获取用户对应的角色
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     * @return 调用认证中心返回结果
     */
    CommonResult login(UmsAdminLoginParam umsAdminLoginParam);


    /**
     * 获取当前登录后台用户
     */
    UmsAdmin getCurrentAdmin() throws ParseException;

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 获取缓存服务
     */
    UmsAdminCacheService getCacheService();

}
