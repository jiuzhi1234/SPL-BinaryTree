package com.jzp.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jzp.common.constant.ProductConstant;
import com.jzp.common.utils.Query;
import com.jzp.gulimail.product.dao.PmsAttrAttrgroupRelationDao;
import com.jzp.gulimail.product.dao.PmsAttrGroupDao;
import com.jzp.gulimail.product.dao.PmsCategoryDao;
import com.jzp.gulimail.product.entity.PmsAttrAttrgroupRelationEntity;
import com.jzp.gulimail.product.entity.PmsAttrGroupEntity;
import com.jzp.gulimail.product.entity.PmsCategoryEntity;
import com.jzp.gulimail.product.service.PmsCategoryService;
import com.jzp.gulimail.product.vo.AttrGroupRelationVo;
import com.jzp.gulimail.product.vo.AttrRespVo;
import com.jzp.gulimail.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzp.common.utils.PageUtils;

import com.jzp.gulimail.product.dao.PmsAttrDao;
import com.jzp.gulimail.product.entity.PmsAttrEntity;
import com.jzp.gulimail.product.service.PmsAttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsAttrService")
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrDao, PmsAttrEntity> implements PmsAttrService {

    @Autowired
    PmsAttrAttrgroupRelationDao pmsAttrAttrgroupRelationDao;

    @Autowired
    PmsAttrGroupDao pmsAttrGroupDao;

    @Autowired
    PmsCategoryDao pmsCategoryDao;

    @Autowired
    PmsCategoryService pmsCategoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        PmsAttrEntity pmsAttrEntity=new PmsAttrEntity();
        BeanUtils.copyProperties(attr,pmsAttrEntity);
        //1.保存基本数据
        this.save(pmsAttrEntity);
        //2.保存关联关系
        if(attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null){
            PmsAttrAttrgroupRelationEntity pmsAttrAttrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
            pmsAttrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            pmsAttrAttrgroupRelationEntity.setAttrId(pmsAttrEntity.getAttrId());
            pmsAttrAttrgroupRelationDao.insert(pmsAttrAttrgroupRelationEntity);
        }


    }

    @Override
    public PageUtils queryBaseAttrPages(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<PmsAttrEntity> queryWrapper = new QueryWrapper<PmsAttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }

        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
               wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils=new PageUtils(page);
        List<PmsAttrEntity> records=page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((pmsAttrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(pmsAttrEntity, attrRespVo);

            //1.设置分类和分组的名字
           if("base".equalsIgnoreCase(type)){
               PmsAttrAttrgroupRelationEntity attrId = pmsAttrAttrgroupRelationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", pmsAttrEntity.getAttrId()));
               if (attrId != null && attrId.getAttrGroupId()!=null) {
                   PmsAttrGroupEntity pmsAttrGroupEntity = pmsAttrGroupDao.selectById(attrId.getAttrGroupId());
                   attrRespVo.setGroupName(pmsAttrGroupEntity.getAttrGroupName());
               }
           }

            PmsCategoryEntity pmsCategoryEntity = pmsCategoryDao.selectById(pmsAttrEntity.getCatelogId());
            if (pmsCategoryEntity != null) {
                attrRespVo.setCatelogName(pmsCategoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo=new AttrRespVo();
        PmsAttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVo);


        if(attrEntity.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1.设置分组信息
            PmsAttrAttrgroupRelationEntity attrgroupRelation = pmsAttrAttrgroupRelationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if(attrgroupRelation!=null){
                respVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
                PmsAttrGroupEntity pmsAttrGroupEntity = pmsAttrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
                if(pmsAttrGroupEntity!=null){
                    respVo.setGroupName(pmsAttrGroupEntity.getAttrGroupName());
                }
            }
        }

        //2.设置分类信息
        Long catelogId=attrEntity.getCatelogId();
        Long[] catelogPath = pmsCategoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(catelogPath);
        PmsCategoryEntity pmsCategoryEntity = pmsCategoryDao.selectById(catelogId);
        if(pmsCategoryEntity!=null){
            respVo.setCatelogName(pmsCategoryEntity.getName());
        }
        return respVo;
    }

    @Override
    public void updateAttr(AttrVo pmsAttr) {
        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(pmsAttr, pmsAttrEntity);
        this.updateById(pmsAttrEntity);

        if(pmsAttrEntity.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {

            //1.修改分组关联
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(pmsAttr.getAttrGroupId());
            relationEntity.setAttrId(pmsAttr.getAttrId());
            Integer count = pmsAttrAttrgroupRelationDao.selectCount(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", pmsAttr.getAttrId()));
            if(count>0){
                pmsAttrAttrgroupRelationDao.update(relationEntity,new UpdateWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id",pmsAttr.getAttrId()));
            }else {
                pmsAttrAttrgroupRelationDao.insert(relationEntity);
            }
        }
        }

    /**
     * 根据分组id查找所有的基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<PmsAttrEntity> getRelationAttr(Long attrgroupId) {
        List<PmsAttrAttrgroupRelationEntity> entities = pmsAttrAttrgroupRelationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> attrIds=entities.stream().map((attr)->{
           return attr.getAttrId();
        }).collect(Collectors.toList());
        if(attrIds==null||attrIds.size()==0){
            return null;
        }
        Collection<PmsAttrEntity> pmsAttrEntities=this.listByIds(attrIds);
        return (List<PmsAttrEntity>) pmsAttrEntities;

    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

//        pmsAttrAttrgroupRelationDao.delete(new QueryWrapper<>().eq("attr_id"))
        List<PmsAttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        pmsAttrAttrgroupRelationDao.deleteBatchRelation(entities);

    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1.当前分组只能关联自己所属的分类里面的所有属性
        PmsAttrGroupEntity attrGroupEntity = pmsAttrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2.当前分组只能关联别的分组没有引用的属性
        //2.1）当前分类下的其他分组
        List<PmsAttrGroupEntity> group = pmsAttrGroupDao.selectList(new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect=group.stream().map(item->{
            return item.getAttrGroupId();
                }).collect(Collectors.toList());
        //2.2)这些分组关联的属性
        List<PmsAttrAttrgroupRelationEntity> groupId = pmsAttrAttrgroupRelationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds=groupId.stream().map(item->{
            return item.getAttrId();
        }).collect(Collectors.toList());
        //2.3）从当前分类的所有属性中移除这些属性
        QueryWrapper<PmsAttrEntity> wrapper = new QueryWrapper<PmsAttrEntity>().eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds!=null && attrIds.size()>0){
            wrapper.notIn("attr_id", attrIds);
        }
        String key =(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<PmsAttrEntity> page=this.page(new Query<PmsAttrEntity>().getPage(params),wrapper);
        PageUtils pageUtils=new PageUtils(page);


        return pageUtils;
    }

    /**
     * 在指定的所有属性集合里，跳出检索属性
     * @param attrIds
     * @return
     */
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {

        return baseMapper.selectSearchAttrIds(attrIds);
    }

}