package com.jxm.upstage.mapper;



import com.jxm.upstage.model.UmsAdmin;

import java.util.List;

public interface UmsAdminMapper {

    int insert(UmsAdmin record);

    List<UmsAdmin> selectAdminByUsername(String username);

}