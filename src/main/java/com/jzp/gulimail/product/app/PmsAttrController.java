package com.jzp.gulimail.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.jzp.gulimail.product.entity.PmsProductAttrValueEntity;
import com.jzp.gulimail.product.service.PmsProductAttrValueService;
import com.jzp.gulimail.product.vo.AttrRespVo;
import com.jzp.gulimail.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jzp.gulimail.product.service.PmsAttrService;
import com.jzp.common.utils.PageUtils;
import com.jzp.common.utils.R;



/**
 * ��Ʒ����
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 17:04:24
 */
@RestController
@RequestMapping("product/pmsattr")
public class PmsAttrController {
    @Autowired
    private PmsAttrService pmsAttrService;

    @Autowired
    PmsProductAttrValueService productAttrValueService;

    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){
        List<PmsProductAttrValueEntity> entities =productAttrValueService.baseAttrlistforspu(spuId);
        return R.ok().put("data", entities);
    }
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String,Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String type){
        PageUtils page =pmsAttrService.queryBaseAttrPages(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsAttrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		PmsAttrEntity pmsAttr = pmsAttrService.getById(attrId);
        AttrRespVo respVo=pmsAttrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		pmsAttrService.saveAttr(attr);

        return R.ok();
    }




    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo pmsAttr){
		pmsAttrService.updateAttr(pmsAttr);

        return R.ok();
    }

    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<PmsProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		pmsAttrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
