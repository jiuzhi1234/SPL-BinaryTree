package com.jzp.gulimail.product.service.impl;

import com.jzp.common.utils.Query;
import com.jzp.gulimail.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzp.common.utils.PageUtils;

import com.jzp.gulimail.product.dao.PmsBrandDao;
import com.jzp.gulimail.product.entity.PmsBrandEntity;
import com.jzp.gulimail.product.service.PmsBrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsBrandService")
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandDao, PmsBrandEntity> implements PmsBrandService {

    @Autowired
    PmsCategoryBrandRelationService pmsCategoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //1.获取key
        String key=(String) params.get("key");
        QueryWrapper<PmsBrandEntity> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key);
        }
        IPage<PmsBrandEntity> page = this.page(
                new Query<PmsBrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(PmsBrandEntity pmsBrand) {
        //保证冗余字段的数据一致
        this.updateById(pmsBrand);
        if(!StringUtils.isEmpty(pmsBrand.getName())){
            //同步更新其他关联表中的数据
            pmsCategoryBrandRelationService.updateBrand(pmsBrand.getBrandId(),pmsBrand.getName());

            //TODO 更新其他关联
        }
    }

}