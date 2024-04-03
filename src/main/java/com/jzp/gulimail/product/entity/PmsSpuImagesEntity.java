package com.jzp.gulimail.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spuͼƬ
 * 
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
@Data
@TableName("pms_spu_images")
public class PmsSpuImagesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * spu_id
	 */
	private Long spuId;
	/**
	 * ͼƬ��
	 */
	private String imgName;
	/**
	 * ͼƬ��ַ
	 */
	private String imgUrl;
	/**
	 * ˳��
	 */
	private Integer imgSort;
	/**
	 * �Ƿ�Ĭ��ͼ
	 */
	private Integer defaultImg;

}
