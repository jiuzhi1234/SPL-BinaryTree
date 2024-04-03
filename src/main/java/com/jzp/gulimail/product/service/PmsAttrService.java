package com.jzp.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzp.common.utils.PageUtils;
import com.jzp.gulimail.product.entity.PmsAttrEntity;
import com.jzp.gulimail.product.vo.AttrGroupRelationVo;
import com.jzp.gulimail.product.vo.AttrRespVo;
import com.jzp.gulimail.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * ��Ʒ����
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
public interface PmsAttrService extends IService<PmsAttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPages(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo pmsAttr);

    List<PmsAttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

