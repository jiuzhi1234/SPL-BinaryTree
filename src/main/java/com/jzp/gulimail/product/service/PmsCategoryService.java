package com.jzp.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzp.common.utils.PageUtils;
import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * ��Ʒ��������
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
public interface PmsCategoryService extends IService<PmsCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PmsCategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);//快捷键ctrl+n可以快速找到某个类

    /**
     * 找到catelogId的完整路径
     * [父/子/孙]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(PmsCategoryEntity pmsCategory);


    List<PmsCategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

