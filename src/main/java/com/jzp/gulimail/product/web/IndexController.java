package com.jzp.gulimail.product.web;

import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.service.PmsCategoryService;
import com.jzp.gulimail.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    PmsCategoryService categoryService;
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        //TODO 1.查出所有的1级分类
        List<PmsCategoryEntity> categoryEntities= categoryService.getLevel1Categorys();

        //视图解析器进行拼串
        //classpath:/templates/ +返回值+ .html
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String,List<Catelog2Vo>> getCatalogJson(){
        Map<String,List<Catelog2Vo>> catalogJson=categoryService.getCatalogJson();
        return catalogJson;
    }



}




















