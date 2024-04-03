package com.jzp.gulimail.product.dao;

import com.jzp.gulimail.product.entity.PmsSpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu��Ϣ
 * 
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
@Mapper
public interface PmsSpuInfoDao extends BaseMapper<PmsSpuInfoEntity> {

    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
