package com.jzp.gulimail.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/*VO 值对象
* 通常用于业务层之间的数据传递，和PO一样仅仅包含数据而已，但应是抽象出来的业务对象
* VO：视图对象：
* 接收页面传来的数据，封装对象
* 将业务处理完成的对象，封装成页面要用的数据
* */
@Data
public class AttrVo {
    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索【0-不需要，1-需要】
     */
    private Integer searchType;
    /**
     * 值类型【0-为单个值，1-可以选择多个值】
     */
    private Integer valueType;
    /**
     *属性图标
     */
    private String icon;
    /**
     * 可选值列表【用逗号分割】
     */
    private String valueSelect;
    /**
     *属性类型【0-销售类型，1-基本属性，2-既是销售属性又是基本属性】
     */
    private Integer attrType;
    /**
     * 启用状态【0-禁用，1-启用】
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上，0-否，1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

    private Long attrGroupId;
}
