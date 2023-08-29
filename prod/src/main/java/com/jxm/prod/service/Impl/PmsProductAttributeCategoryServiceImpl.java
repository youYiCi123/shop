package com.jxm.prod.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.prod.dto.PmsProductAttributeCategoryItem;
import com.jxm.prod.mapper.PmsProductAttributeCategoryDao;
import com.jxm.prod.mapper.PmsProductAttributeCategoryMapper;
import com.jxm.prod.model.PmsProductAttributeCategory;
import com.jxm.prod.service.PmsProductAttributeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Autowired
    private PmsProductAttributeCategoryDao productAttributeCategoryDao;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return productAttributeCategoryMapper.insert(productAttributeCategory);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return productAttributeCategoryMapper.update(productAttributeCategory);
    }

    @Override
    public int delete(Long id) {
        return productAttributeCategoryMapper.deleteById(id);
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeCategoryMapper.selectAll();
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }

}
