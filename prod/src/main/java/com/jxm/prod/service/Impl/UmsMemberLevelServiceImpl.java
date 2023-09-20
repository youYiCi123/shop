package com.jxm.prod.service.Impl;

import com.jxm.prod.mapper.UmsMemberLevelMapper;
import com.jxm.prod.model.UmsMemberLevel;
import com.jxm.prod.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员等级管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {
    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;
    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        return memberLevelMapper.selectByDefaultStatus(defaultStatus);
    }
}
