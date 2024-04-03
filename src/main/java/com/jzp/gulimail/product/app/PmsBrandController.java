package com.jzp.gulimail.product.app;

import java.util.Arrays;
import java.util.Map;


import com.jzp.common.valid.AddGroup;
import com.jzp.common.valid.UpdateGroup;
import com.jzp.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jzp.gulimail.product.entity.PmsBrandEntity;
import com.jzp.gulimail.product.service.PmsBrandService;
import com.jzp.common.utils.PageUtils;
import com.jzp.common.utils.R;


/**
 * Ʒ��
 *
 * @author jzp
 * @email 2745588458@qq.com
 * @date 2024-03-13 17:04:24
 */
@RestController
@RequestMapping("product/pmsbrand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService pmsBrandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsBrandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		PmsBrandEntity pmsBrand = pmsBrandService.getById(brandId);

        return R.ok().put("pmsBrand", pmsBrand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody PmsBrandEntity pmsBrand/*,BindingResult result*/){//@Validated注解表示参数需要分组校验
//		if(result.hasErrors()){
//            Map<String,String> map=new HashMap<>();
//            //1.获取校验的错误信息
//            result.getFieldErrors().forEach((item)->{
//                //FieldError 获取到错误提示
//                String message =item.getDefaultMessage();
//                //获取错误属性的名字
//                String field=item.getField();
//                map.put(field,message);
//            });
//            return R.error(400,"提交的数据不合法").put("data",map);
//        }else {
//
//        }
        pmsBrandService.save(pmsBrand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody PmsBrandEntity pmsBrand){
		pmsBrandService.updateDetail(pmsBrand);

        return R.ok();
    }
    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody PmsBrandEntity pmsBrand){
        pmsBrandService.updateById(pmsBrand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		pmsBrandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
