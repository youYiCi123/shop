package com.jxm.upstage.mapper;


import com.jxm.upstage.model.UmsAdminRoleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsAdminRoleRelationMapper {

    int insert(UmsAdminRoleRelation record);

    int deleteById(Long adminId);

    List<Long> selectById(Long id);

    int deleteByAdminIds(@Param("ids") List<Long> ids);

    List<UmsAdminRoleRelation> selectRoleListByAdminId(@Param("adminIds") List<Long> adminIds);
}