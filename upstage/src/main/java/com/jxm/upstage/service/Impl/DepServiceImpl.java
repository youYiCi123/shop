package com.jxm.upstage.service.Impl;
import cn.hutool.crypto.digest.BCrypt;
import com.github.pagehelper.PageHelper;
import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.upstage.dto.*;
import com.jxm.upstage.mapper.DepMapper;
import com.jxm.upstage.mapper.UmsAdminMapper;
import com.jxm.upstage.mapper.UmsAdminRoleRelationMapper;
import com.jxm.upstage.model.Dep;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.model.UmsAdminRoleRelation;
import com.jxm.upstage.service.DepService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepServiceImpl implements DepService {

    @Autowired
    private DepMapper depMapper;

    @Autowired
    private UmsAdminMapper umsAdminMapper;

    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;


    @Override
    public Dep add(DepParam depParam) {
        Dep dep=new Dep();
        BeanUtils.copyProperties(depParam, dep);
        UniqueIdGenerator uniqueId = new UniqueIdGenerator(1,1);
        long depId = uniqueId.nextId();
        dep.setId(depId);
        dep.setCreateTime(new Date());
        depMapper.insert(dep);
        String leadPhone=dep.getLeadPhone();
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectAdminByUsername(leadPhone);
        if(umsAdmins.size()==0){  //用户表中没有该用户
            //添加用户
            UmsAdmin umsAdmin=new UmsAdmin();
            long userId = uniqueId.nextId();
            umsAdmin.setId(userId);
            umsAdmin.setUsername(leadPhone);
            umsAdmin.setSex(depParam.getLeadSex());
            umsAdmin.setDuty(dep.getDepName()+" 负责人");
            umsAdmin.setPassword(BCrypt.hashpw("123456"));
            umsAdmin.setEmail(depParam.getLeadEmail());
            umsAdmin.setDepId(depId);
            umsAdmin.setCreateTime(new Date());
            umsAdmin.setNickName(dep.getLeadName());
            umsAdmin.setStatus(1);
            umsAdminMapper.insert(umsAdmin);
            //分配超管角色
            UmsAdminRoleRelation umsAdminRoleRelation= new UmsAdminRoleRelation();
            umsAdminRoleRelation.setRoleId(Long.valueOf(5));
            umsAdminRoleRelation.setAdminId(userId);
            umsAdminRoleRelationMapper.insert(umsAdminRoleRelation);
        }
        //创建文件根目录  todo
//        iUserFileService.createFolder(CommonConstant.ZERO_LONG, FileConstant.ALL_FILE_CN_STR, emailServiceImpl.getLoginUserId(),depStrId);
        return dep;
    }

    @Override
    public List<Dep> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Dep> depList= depMapper.list(keyword);
        return depList;
    }

    @Override
    public List<DepIdToName> getDepIdToName() {
        return depMapper.getDepIdToName();
    }

    @Override
    public Dep getDepByDeptId(Long deptId) {
      return depMapper.getDepByDeptId(deptId);
    }

    @Override
    public List<DepUser> details(Long depId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DepUser> depUsers= umsAdminMapper.details(depId);
        return depUsers;
    }

    @Override
    public int update(Long id, Dep dep) {
        dep.setId(id);
        return depMapper.update(dep);
    }

    @Override
    public int delete(Long id){
        return depMapper.delete(id);
    }
    @Override
    public int deleteBatch(List<Long> idList) {
        return depMapper.deleteBatch(idList);
    }

    @Override
    public List<depUserRelation> selectUserRelation(){
        return  depMapper.selectUserRelation();
    }
}
