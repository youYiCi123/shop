package com.jxm.upstage.mapper;

import com.jxm.upstage.dto.DepIdToName;
import com.jxm.upstage.dto.depUserRelation;
import com.jxm.upstage.model.Dep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepMapper {

    int insert(Dep dep);

    List<Dep> list(@Param("keyword") String keyword);

    List<DepIdToName> getDepIdToName();

    Dep getDepByDeptId(@Param("deptId") Long depId);

    int update(Dep dep);

    int delete(Long id);

    int deleteBatch(@Param("idList") List<Long> idList);

    List<depUserRelation> selectUserRelation();

    Long selectDepHeadIdByUser(Long userId);
}
