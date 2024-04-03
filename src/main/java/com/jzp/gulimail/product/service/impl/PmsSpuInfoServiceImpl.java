package com.jzp.gulimail.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jzp.common.constant.ProductConstant;
import com.jzp.common.to.SkuHasStockVo;
import com.jzp.common.to.SkuReductionTo;
import com.jzp.common.to.SpuBoundTo;
import com.jzp.common.to.es.SkuEsModel;
import com.jzp.common.utils.Query;
import com.jzp.common.utils.R;
import com.jzp.gulimail.product.entity.*;
import com.jzp.gulimail.product.feign.CouponFeignService;
import com.jzp.gulimail.product.feign.SearchFeignService;
import com.jzp.gulimail.product.feign.WareFeignService;
import com.jzp.gulimail.product.service.*;
import com.jzp.gulimail.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzp.common.utils.PageUtils;

import com.jzp.gulimail.product.dao.PmsSpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuInfoService")
public class PmsSpuInfoServiceImpl extends ServiceImpl<PmsSpuInfoDao, PmsSpuInfoEntity> implements PmsSpuInfoService {

    @Autowired
    PmsSpuInfoDescService spuInfoDescService;
    @Autowired
    PmsSpuImagesService imagesService;
    @Autowired
    PmsAttrService attrService;
    @Autowired
    PmsProductAttrValueService attrValueService;
    @Autowired
    PmsSkuInfoService skuInfoService;
    @Autowired
    PmsSkuImagesService skuImagesService;
    @Autowired
    PmsSkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    PmsBrandService brandService;
    @Autowired
    PmsCategoryService categoryService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    SearchFeignService searchFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * TODO 高级部分继续完善
     * @param vo
     */

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1.保存spu基本信息 pms_sou_info
        PmsSpuInfoEntity infoEntity=new PmsSpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2.保存spu的描述图片 pms_spu_info_desc
        List<String> descript =vo.getDecript();
        PmsSpuInfoDescEntity descEntity=new PmsSpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",descript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        //3.保存spu的图片集,pms_spu_images
        List<String> images=vo.getImages();
        imagesService.saveImages(infoEntity.getId(),images);

        //4.保存spu的规格参数，pms_product_attr_value
        List<BaseAttrs> baseAttrs=vo.getBaseAttrs();
        List<PmsProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            PmsProductAttrValueEntity valueEntity = new PmsProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            PmsAttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        //5.保存spu的积分信息 gulimail_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode()!=0){
            log.error("远程保存spu积分信息失败");
        }

        //5.保存当前spu对应的所有sku信息
        List<Skus> skus=vo.getSkus();
        if(skus!=null&&skus.size()>0){
            skus.forEach(item->{
                String defaultImg="";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg()==1){
                        defaultImg=image.getImgUrl();
                    }
                }
                PmsSkuInfoEntity skuInfoEntity = new PmsSkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //5.1）、sku的基本信息 pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId=skuInfoEntity.getSkuId();

                List<PmsSkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    PmsSkuImagesEntity skuImagesEntity = new PmsSkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                        }).collect(Collectors.toList());

                //5.2)、sku的图片信息 pms_sku_images
                skuImagesService.saveBatch(imagesEntities);
                //TODO 没有图片；路径无需保存

                List<Attr> attr = item.getAttr();
                List<PmsSkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    PmsSkuSaleAttrValueEntity attrValueEntity = new PmsSkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                //5.3)、sku的基本销售属性信息 pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //5.4)sku的优惠、满减等信息 gulimail_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode()!=0){
                        log.error("远程保存spu优惠信息失败");
                    }
                }

            });
        }

    }

    @Override
    public void saveBaseSpuInfo(PmsSpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PmsSpuInfoEntity> wrapper = new QueryWrapper<>();
        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status=(String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId=(String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId=(String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                wrapper

        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        //1.查出当前spuid对应的所有sku信息，品牌的名字
        List<PmsSkuInfoEntity> skus=skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(PmsSkuInfoEntity::getSkuId).collect(Collectors.toList());

        //TODO 4.查询当前sku的所有可以被用来检索的规格属性
        List<PmsProductAttrValueEntity> baseAttrs = attrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttrIds=attrService.selectSearchAttrIds(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());


        //TODO 1.发送远程调用，库存系统查询是否有库存
        Map<Long,Boolean> stockMap=null;
       try {
           R r = wareFeignService.getSkusHasStock(skuIdList);
           TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
           };
           stockMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
       }catch (Exception e){
           log.error("库存服务查询异常：原因{}",e);
       }
        //2.封装每个sku的信息
        Map<Long,Boolean> finalStockMap=stockMap;
        List<SkuEsModel> upProducts=skus.stream().map(sku->{
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);

            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            //设置库存信息
            if(finalStockMap ==null){//java要求此处的值是一个final的，前面不能进行赋值了
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //TODO 2.热度评分 0
            esModel.setHotScore(0L);

            //TODO 3.查询品牌和分类的名字信息
            PmsBrandEntity brand = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());
            PmsCategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(category.getName());

            //设置检索属性
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());
        //TODO 5.将数据发送给es进行保存：gulimaiil-search
        R r = searchFeignService.productStatus(upProducts);
        if(r.getCode()==0){
            //远程调用成功
            //TODO 6.修改当前spu的状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_IP.getCode());
        }else {
            //远程调用失败
            //TODO 7.重复调用？接口幂等性；重试机制
            //Feign调用流程
            /**
             * 1.构造请求数据，将对象转为json
             *   RequestTemplate template=buildTemplateFromArgs.create(argv);
             * 2.发送请求进行执行（执行成功会解码响应数据）：
             *   executeAndDecode（template）
             * 3.执行请求会有重试机制
             *   while(true){
             *       try{
             *           executeAndDecode(template);
             *       }catch(){
             *           try{retryer.continueOrPropagate(e);}catch(){throw ex;}
             *           continue;
             *       }
             *   }
             */
        }


    }

}

















