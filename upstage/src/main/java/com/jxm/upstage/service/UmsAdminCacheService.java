package com.jxm.upstage.service;

import com.jxm.upstage.dto.OnlineUmsAdmin;
import com.jxm.upstage.model.UmsAdmin;

import java.util.List;

/**
 * 后台用户缓存操作类
 * Created by macro on 2020/3/13.
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     */
    void delResourceList(Long adminId);

    /**
     * 获取缓存后台用户信息
     */
    OnlineUmsAdmin getAdmin(Long adminId);

    /**
     * 获取所有缓存的用户信息
     */
    List<OnlineUmsAdmin> getAll();
    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(OnlineUmsAdmin admin);
}
