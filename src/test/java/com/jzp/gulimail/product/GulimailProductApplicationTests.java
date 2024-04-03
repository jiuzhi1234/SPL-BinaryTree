package com.jzp.gulimail.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jzp.gulimail.product.entity.PmsBrandEntity;
import com.jzp.gulimail.product.service.PmsBrandService;
import com.jzp.gulimail.product.service.PmsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailProductApplicationTests {

    @Autowired
    PmsBrandService pmsBrandService;
    @Autowired
    PmsCategoryService pmsCategoryService;
    @Test
    public void testFindPath(){
        Long[] catelogPath=pmsCategoryService.findCatelogPath(1437L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }
    @Test
    public void testUpload() throws FileNotFoundException {
//        String endpoint = ConstantPropertiesUtils.END_POINT;
//        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
//        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
////        String endpoint="oss-cn-beijing.aliyuncs.com";
////        //云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或
////        String accessKeyId="LTAI5t5b7NbUJodQJVVtE17X";
////        String accessKeySecret="QIlG83XwXtTqcu6uKC4EX0Xyg8KKW0";
//        //创建OSSClient实例
//        OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
//        //上传文件流
//        InputStream inputStream=new FileInputStream("C:\\Users\\27455\\Pictures\\Screenshots\\屏幕截图 2023-03-13 122058.png");
//        ossClient.putObject("gulimail-jzp","gg.jpg",inputStream);
//        //关闭OSSClient
//        ossClient.shutdown();
//        System.out.println("上传完成");

    }
    @Test
    public void contextLoads() {
      // PmsBrandEntity pmsBrandEntity=new PmsBrandEntity();
//        pmsBrandEntity.setName("华为");
//        pmsBrandService.save(pmsBrandEntity);
//        System.out.println("保存成功");
//        pmsBrandEntity.setBrandId(1L);
//        pmsBrandEntity.setDescript("华为");
//        pmsBrandService.updateById(pmsBrandEntity);
        List<PmsBrandEntity> list = pmsBrandService.list(new QueryWrapper<PmsBrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->{
            System.out.println(item);
        });
    }

}
