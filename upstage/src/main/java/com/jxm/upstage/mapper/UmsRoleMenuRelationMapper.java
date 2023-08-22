package com.jxm.upstage.mapper;

import com.jxm.upstage.model.UmsRoleMenuRelation;

;

public interface UmsRoleMenuRelationMapper {

    int deleteByRoleId(Long roleId);

    int insert(UmsRoleMenuRelation record);

}