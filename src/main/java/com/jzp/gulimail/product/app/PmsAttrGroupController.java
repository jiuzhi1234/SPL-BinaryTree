package com.jzp.gulimail.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.jzp.gulimail.product.entity.PmsAttrEntity;
import com.jzp.gulimail.product.service.PmsAttrAttrgroupRelationService;
import com.jzp.gulimail.product.service.PmsAttrService;
import com.jzp.gulimail.product.service.PmsCategoryService;
import com.jzp.gulimail.product.vo.AttrGroupRelationVo;
import com.jzp.gulimail.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jzp.gulimail.product.entity.PmsAttrGroupEntity;
import com.jzp.gulimail.product.service.PmsAttrGroupService;
import com.jzp.common.utils.PageUtils;
import com.jzp.common.utils.R;



/**
 * ���Է���
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 17:04:24
 */
@RestController
@RequestMapping("product/pmsattrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;
    @Autowired
    private PmsCategoryService pmsCategoryService;

    @Autowired
    PmsAttrService pmsAttrService;

    @Autowired
    PmsAttrAttrgroupRelationService relationService;

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){
        //1.查出当前分类下的所有属性分组
        //2.查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos= pmsAttrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }


    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<PmsAttrEntity> entities=pmsAttrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String,Object> params){
        PageUtils page=pmsAttrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        pmsAttrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId")Long catelogId){
//        PageUtils page = pmsAttrGroupService.queryPage(params);
        PageUtils page= pmsAttrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);
        Long catelogId = pmsAttrGroup.getCatelogId();
        Long[] path=pmsCategoryService.findCatelogPath(catelogId);
        pmsAttrGroup.setCatelogPath(path);

        return R.ok().put("pmsAttrGroup", pmsAttrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.save(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.updateById(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		pmsAttrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
