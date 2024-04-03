package com.jzp.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzp.common.utils.PageUtils;
import com.jzp.gulimail.product.entity.PmsBrandEntity;
import com.jzp.gulimail.product.entity.PmsCategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * Ʒ�Ʒ������
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<PmsBrandEntity> getBrandsByCatId(Long catId);
}

