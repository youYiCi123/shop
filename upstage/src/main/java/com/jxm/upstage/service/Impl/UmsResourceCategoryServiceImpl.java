package com.jxm.upstage.service.Impl;

import com.jxm.upstage.mapper.UmsResourceCategoryMapper;
import com.jxm.upstage.model.UmsResourceCategory;
import com.jxm.upstage.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理Service实现类
 * Created by macro on 2020/2/5.
 */
@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    private UmsResourceCategoryMapper resourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        return resourceCategoryMapper.selectResourceCategory();
    }

}
