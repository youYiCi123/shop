package com.jxm.prod.mapper;

import com.jxm.prod.model.CmsSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsSubjectMapper {

    List<CmsSubject> selectAll();

    List<CmsSubject> selectByTitle(String keyword);

}