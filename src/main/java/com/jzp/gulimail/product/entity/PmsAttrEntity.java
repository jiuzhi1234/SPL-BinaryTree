package com.jzp.gulimail.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ��Ʒ����
 * 
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
@Data
@TableName("pms_attr")
public class PmsAttrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 属性id
	 */
	@TableId
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



}
