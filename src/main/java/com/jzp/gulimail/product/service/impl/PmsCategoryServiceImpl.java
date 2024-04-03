package com.jzp.gulimail.product.service.impl;

import com.jzp.common.utils.Query;
import com.jzp.gulimail.product.service.PmsCategoryBrandRelationService;
import com.jzp.gulimail.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzp.common.utils.PageUtils;

import com.jzp.gulimail.product.dao.PmsCategoryDao;
import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.service.PmsCategoryService;
import org.springframework.transaction.annotation.Transactional;

//该包下存放的都是对应service的实现类
@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

    @Autowired
    PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
        //1.查出所有分类
        List<PmsCategoryEntity> entities=baseMapper.selectList(null);//BaseMapper是MyBatis-plus集成的一个方法，主要是集成了一些基本的CRUD的方法，和普通的Mapper一样。
        //2.组装成父子的树形结构


        //2.1找到所有的一级分类
        List<PmsCategoryEntity> levelMenus=entities.stream().filter(pmsCategoryEntity ->
                 pmsCategoryEntity.getParentCid()==0
                ).map((menu)->{
                    menu.setChildren(getChildrens(menu,entities));
                    return menu;
                }).sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());//对根菜单进行排序
                }).collect(Collectors.toList());//map的作用是对levelMenus集合进行遍历，之后再对每一项进行对应处理，再将处理后的menu赋值给levelMenus

        return levelMenus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO //1.检查当前删除的菜单，是否被其他别的地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return  parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param pmsCategory
     */
    @Transactional
    @Override
    public void updateCascade(PmsCategoryEntity pmsCategory) {
        this.updateById(pmsCategory);
        pmsCategoryBrandRelationService.updateCategory(pmsCategory.getCatId(),pmsCategory.getName());
    }

    @Override
    public List<PmsCategoryEntity> getLevel1Categorys() {
        List<PmsCategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;

    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        //1.查出所有1级分类
        List<PmsCategoryEntity> level1Categorys=getLevel1Categorys();
        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每一个的一级分类，查到这个一级分类的二级分类
            List<PmsCategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", v.getCatId()));
            //2.封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(),null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类封装成vo
                    List<PmsCategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", l2.getCatId()));
                    if(level3Catelog!=null){
                        List<Catelog2Vo.Catelog3Vo> collect=level3Catelog.stream().map(l3->{
                            //2.封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo=new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);

                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        return parent_cid;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1.收集当前节点id
        paths.add(catelogId);
        PmsCategoryEntity byId=this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    //递归查找所有菜单的子菜单:第一个参数为当前菜单，第二个参数为所有菜单数据
    private List<PmsCategoryEntity> getChildrens(PmsCategoryEntity root,List<PmsCategoryEntity> all){
        List<PmsCategoryEntity> children=all.stream().filter(pmsCategoryEntity -> {
            return pmsCategoryEntity.getParentCid()==root.getCatId();
        }).map(pmsCategoryEntity -> {
            //1.找到子菜单
            pmsCategoryEntity.setChildren(getChildrens(pmsCategoryEntity,all));
            return pmsCategoryEntity;
        }).sorted((menu1,menu2)->{
            //2.菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

}
















