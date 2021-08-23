package com.zy.orderserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderInfoController {

//    @Autowired
//    private OrderInfoMapper orderInfoMapper;

    @RequestMapping("/selectOrderInfoByIdAndUserName/{orderNo}")
    public Object selectOrderInfoById(@PathVariable("orderNo") String orderNo,@RequestHeader("userName") String userName) {
        log.info("userName:{}，orderNo={}",userName,orderNo);

//        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoByIdAndUserName(orderNo,userName);
//        if(null == orderInfo) {
//            return "根据orderNo:"+orderNo+"查询没有该订单";
//        }
        return "成功";
    }

    @RequestMapping("/saveOrder")
    public Object saveOrder() {
        log.info("保存订单");
        return "保存订单";
    }


}
