package com.jzp.gulimail.product.feign;

import com.jzp.common.to.SkuHasStockVo;
import com.jzp.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimail-ware")
public interface WareFeignService {
    /**
     * 1.R设计的时候可以加上泛型
     * 2.直接返回我们想要的结果
     * 3.自己封装解析结果
     * @param skuIds
     * @return
     */

    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
