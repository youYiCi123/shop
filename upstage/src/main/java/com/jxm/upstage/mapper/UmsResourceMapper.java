package com.jxm.upstage.mapper;

import com.jxm.upstage.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsResourceMapper {

    List<UmsResource> selectAllResource();

}