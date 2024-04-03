package com.jzp.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzp.common.utils.PageUtils;
import com.jzp.gulimail.product.entity.PmsSpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spuͼƬ
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 16:17:52
 */
public interface PmsSpuImagesService extends IService<PmsSpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

