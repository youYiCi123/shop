package com.jxm.upstage.mapper;



import com.jxm.upstage.model.UmsAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsAdminMapper {

    int insert(UmsAdmin record);

    List<UmsAdmin> selectByQuery(@Param("keyword") String keyword);

    int deleteByPrimaryKey(Long id);

    int deleteBatch(@Param("idList") List<Long> idList);

    List<UmsAdmin> selectAdminByUsername(String username);

    UmsAdmin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsAdmin record);

    List<String> getAllUserPhone();

    int saveBatch(@Param("list") List<UmsAdmin> list);
}