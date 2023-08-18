package com.jxm.upstage.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.upstage.mapper.UmsRoleDao;
import com.jxm.upstage.mapper.UmsRoleMapper;
import com.jxm.upstage.model.UmsMenu;
import com.jxm.upstage.model.UmsRole;
import com.jxm.upstage.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 后台角色管理Service实现类
 * Created by macro on 2018/9/30.
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleDao roleDao;

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return roleDao.getMenuList(adminId);
    }

}
