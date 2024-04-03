package com.jzp.gulimail.product.feign;

import com.jzp.common.to.es.SkuEsModel;
import com.jzp.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimail-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatus(@RequestBody List<SkuEsModel> skuEsModels);
}
