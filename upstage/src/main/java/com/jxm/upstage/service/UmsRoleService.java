package com.jxm.upstage.service;

import com.jxm.upstage.model.UmsMenu;
import com.jxm.upstage.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色管理Service
 * Created by macro on 2018/9/30.
 */
public interface UmsRoleService {

    /**
     * 获取所有角色列表
     */
    List<UmsRole> list();

    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long adminId);

}
