package com.zy.password.entity;

import lombok.Data;

/**
 * @author: zy
 * @date: 2021/8/18 下午3:28
 * @description:
 */
@Data
public class ProductInfo {
    private String productNo;
    private String productName;
    private Integer productCount;
    private Integer productPrice;
}
