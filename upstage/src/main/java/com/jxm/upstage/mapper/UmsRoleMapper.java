package com.jxm.upstage.mapper;

import com.jxm.upstage.dto.RoleGroupCount;
import com.jxm.upstage.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsRoleMapper {

    int insert(UmsRole record);

    int updateByPrimaryKey(UmsRole record);

    int deleteByIds(@Param("ids") List<Long> ids);

    List<UmsRole> selectAllRole();

    List<UmsRole> selectByName(String keyword);

    int addAdminCount(@Param("roleId") List<Long> roleId);

    int subAdminCount(@Param("roleId") List<Long> roleId);

    int subAdminCountByClass(@Param("RoleGroupCounts") List<RoleGroupCount> RoleGroupCounts);
}