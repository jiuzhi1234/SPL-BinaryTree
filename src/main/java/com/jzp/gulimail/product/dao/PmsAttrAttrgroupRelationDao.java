package com.jzp.gulimail.product.dao;

import com.jzp.gulimail.product.entity.PmsAttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ����&���Է������
 * 
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
@Mapper
public interface PmsAttrAttrgroupRelationDao extends BaseMapper<PmsAttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities") List<PmsAttrAttrgroupRelationEntity> entities);
}
