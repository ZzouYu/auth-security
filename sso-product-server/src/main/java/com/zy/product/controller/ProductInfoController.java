package com.zy.product.controller;

import com.zy.product.entity.ProductInfo;
import com.zy.product.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductInfoController {


    @RequestMapping("/selectProductInfoById/{productNo}")
    public Result<ProductInfo> selectProductInfoById(@PathVariable("productNo") String productNo) {
        log.info("productNo={}",productNo);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductCount(1);
        productInfo.setProductName("iphone 13");
        productInfo.setProductNo("1111");
        productInfo.setProductPrice(12345);
        return Result.success(productInfo);
    }


}
