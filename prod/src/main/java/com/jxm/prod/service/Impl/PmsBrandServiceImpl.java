package com.jxm.prod.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.prod.dto.PmsBrandParam;
import com.jxm.prod.mapper.PmsBrandMapper;
import com.jxm.prod.mapper.PmsProductMapper;
import com.jxm.prod.model.PmsBrand;
import com.jxm.prod.model.PmsProduct;
import com.jxm.prod.service.PmsBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品品牌Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService {
    @Autowired
    private PmsBrandMapper brandMapper;
    @Autowired
    private PmsProductMapper productMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectAll();
    }

    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return brandMapper.insertBrand(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        //更新品牌时要更新商品中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        productMapper.updateByBrandId(product,id);
        return brandMapper.update(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteById(id);
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        return brandMapper.deleteBatch(ids);
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return brandMapper.selectByName(keyword);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectById(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        return brandMapper.updateByExampleSelective(pmsBrand, ids);
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);
        return brandMapper.updateByExampleSelective(pmsBrand, ids);
    }
}
