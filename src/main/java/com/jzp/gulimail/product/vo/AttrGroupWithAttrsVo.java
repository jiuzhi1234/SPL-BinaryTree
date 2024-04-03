package com.jzp.gulimail.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.jzp.gulimail.product.entity.PmsAttrEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo {

    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    private List<PmsAttrEntity> attrs;
}
