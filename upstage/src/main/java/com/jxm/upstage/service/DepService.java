package com.jxm.upstage.service;


import com.jxm.upstage.dto.DepIdToName;
import com.jxm.upstage.dto.DepParam;
import com.jxm.upstage.dto.DepUser;
import com.jxm.upstage.dto.depUserRelation;
import com.jxm.upstage.model.Dep;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepService {

    /**
     * 部门添加功能
     */
    Dep add(DepParam depParam);

    /**
     * 根据部门名分页查询部门
     */
    List<Dep> list(String keyword, Integer pageSize, Integer pageNum);

    List<DepIdToName> getDepIdToName();

    Dep getDepByDeptId(Long deptId);

    /**
     * 根据部门id查询该部门下的所有人员
     */
    List<DepUser> details(Long deptId, Integer pageSize, Integer pageNum);

    /**
     * 修改指定部门信息
     */
    int update(Long id, Dep dep);

    /**
     * 删除指定部门
     */
    int delete(Long id);

    /**
     * 批量删除指定用户
     */
    @Transactional
    int deleteBatch(List<Long> idList);

    /**
    查询所有部门和人员信息级联关系
     */
    List<depUserRelation> selectUserRelation();

    Long selectDepHeadIdByUser(Long userId);

}
