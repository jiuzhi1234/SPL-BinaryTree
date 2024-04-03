package com.jzp.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jzp.common.utils.Query;
import com.jzp.gulimail.product.dao.PmsBrandDao;
import com.jzp.gulimail.product.dao.PmsCategoryDao;
import com.jzp.gulimail.product.entity.PmsBrandEntity;
import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzp.common.utils.PageUtils;

import com.jzp.gulimail.product.dao.PmsCategoryBrandRelationDao;
import com.jzp.gulimail.product.entity.PmsCategoryBrandRelationEntity;
import com.jzp.gulimail.product.service.PmsCategoryBrandRelationService;


@Service("pmsCategoryBrandRelationService")
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {

    @Autowired
    PmsBrandDao pmsbrandDao;
    @Autowired
    PmsCategoryDao pmsCategoryDao;

    @Autowired
    PmsCategoryBrandRelationDao relationDao;

    @Autowired
    PmsBrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        Long brandId = pmsCategoryBrandRelation.getBrandId();
        Long catelogId = pmsCategoryBrandRelation.getCatelogId();
        //1.查询详细名字
        PmsBrandEntity pmsBrandEntity = pmsbrandDao.selectById(brandId);
        PmsCategoryEntity pmsCategoryEntity = pmsCategoryDao.selectById(catelogId);
        pmsCategoryBrandRelation.setBrandName(pmsBrandEntity.getName());
        pmsCategoryBrandRelation.setCatelogName(pmsCategoryEntity.getName());
        this.save(pmsCategoryBrandRelation);

    }

    @Override
    public void updateBrand(Long brandId, String name) {
        PmsCategoryBrandRelationEntity pmsCategoryBrandRelationEntity = new PmsCategoryBrandRelationEntity();
        pmsCategoryBrandRelationEntity.setBrandId(brandId);
        pmsCategoryBrandRelationEntity.setBrandName(name);
        this.update(pmsCategoryBrandRelationEntity,new UpdateWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

    @Override
    public List<PmsBrandEntity> getBrandsByCatId(Long catId) {
        List<PmsCategoryBrandRelationEntity> catelogId = relationDao.selectList(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<PmsBrandEntity> collect = catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            PmsBrandEntity byId = brandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());
        return collect;
    }

}