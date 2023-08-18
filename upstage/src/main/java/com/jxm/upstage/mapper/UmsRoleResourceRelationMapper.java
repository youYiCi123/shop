package com.jxm.upstage.mapper;

import com.jxm.upstage.model.UmsRoleResourceRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsRoleResourceRelationMapper {

    List<UmsRoleResourceRelation> selectAllRoleResource();

}