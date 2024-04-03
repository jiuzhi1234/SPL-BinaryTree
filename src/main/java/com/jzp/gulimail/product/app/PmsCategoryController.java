package com.jzp.gulimail.product.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.service.PmsCategoryService;
import com.jzp.common.utils.R;



/**
 * ��Ʒ��������
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 17:04:25
 */
@RestController
@RequestMapping("product/pmscategory")
public class PmsCategoryController {
    @Autowired
    private PmsCategoryService pmsCategoryService;

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list(){
       List<PmsCategoryEntity> entities=pmsCategoryService.listWithTree();

        return R.ok().put("page",entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		PmsCategoryEntity pmsCategory = pmsCategoryService.getById(catId);

        return R.ok().put("data", pmsCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.save(pmsCategory);

        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody PmsCategoryEntity[] pmsCategory){
        pmsCategoryService.updateBatchById(Arrays.asList(pmsCategory));

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.updateCascade(pmsCategory);

        return R.ok();
    }

    /**
     * 删除
     * @RequestBody:获取请求体，必须发送post请求，因为get请求不带请求体
     * SpringMVC自动将请求体的数据（json），转为对应的对象
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
        //1.检查当前删除的菜单，是否被其他别的地方引用
//		pmsCategoryService.removeByIds(Arrays.asList(catIds));
        pmsCategoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
