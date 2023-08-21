package com.jxm.upstage.mapper;

import com.jxm.upstage.dto.RoleGroupCount;
import com.jxm.upstage.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsRoleMapper {

    List<UmsRole> selectAllRole();

    int addAdminCount(@Param("roleId") List<Long> roleId);

    int subAdminCount(@Param("roleId") List<Long> roleId);

    int subAdminCountByClass(@Param("RoleGroupCounts") List<RoleGroupCount> RoleGroupCounts);
}