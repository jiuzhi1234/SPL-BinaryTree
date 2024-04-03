package com.jzp.gulimail.product.dao;

import com.jzp.gulimail.product.entity.PmsCategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Ʒ�Ʒ������
 * 
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
@Mapper
public interface PmsCategoryBrandRelationDao extends BaseMapper<PmsCategoryBrandRelationEntity> {

    //mybatis声名的接口如果有两个参数，一定要事先使用Params注解声明一下，否则要在sql语句中拿到两个参数很会很困难
    void updateCategory(@Param("catId") Long catId,@Param("name") String name);
}
