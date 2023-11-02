package com.jxm.upstage.mapper;



import com.jxm.upstage.dto.DepUser;
import com.jxm.upstage.dto.UmsAdminConcat;
import com.jxm.upstage.model.UmsAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsAdminMapper {

    int insert(UmsAdmin record);

    List<UmsAdmin> selectByQuery(@Param("keyword") String keyword,@Param("depId") Long depId);

    int deleteByPrimaryKey(Long id);

    int deleteBatch(@Param("idList") List<Long> idList);

    List<UmsAdmin> selectAdminByUsername(String username);

    UmsAdmin selectByPrimaryKey(Long id);

    UmsAdminConcat getUmsAdminConcat(Long id);

    int updateByPrimaryKeySelective(UmsAdmin record);

    List<String> getAllUserPhone();

    int saveBatch(@Param("list") List<UmsAdmin> list);

    List<DepUser> details(@Param("depId") Long depId);
}