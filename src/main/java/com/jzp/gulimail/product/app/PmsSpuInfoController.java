package com.jzp.gulimail.product.app;

import java.util.Arrays;
import java.util.Map;


import com.jzp.gulimail.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jzp.gulimail.product.entity.PmsSpuInfoEntity;
import com.jzp.gulimail.product.service.PmsSpuInfoService;
import com.jzp.common.utils.PageUtils;
import com.jzp.common.utils.R;



/**
 * spu��Ϣ
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 17:04:25
 */
@RestController
@RequestMapping("product/pmsspuinfo")
public class PmsSpuInfoController {
    @Autowired
    private PmsSpuInfoService pmsSpuInfoService;

    //商品上架
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("supId") Long spuId){
        pmsSpuInfoService.up(spuId);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsSpuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PmsSpuInfoEntity pmsSpuInfo = pmsSpuInfoService.getById(id);

        return R.ok().put("pmsSpuInfo", pmsSpuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsSpuInfoEntity pmsSpuInfo){
		pmsSpuInfoService.updateById(pmsSpuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo vo){
		pmsSpuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		pmsSpuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
