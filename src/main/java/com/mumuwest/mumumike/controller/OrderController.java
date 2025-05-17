package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController("/order")
public class OrderController {

    public AjaxResult getOrderById(Integer id) {
        return AjaxResult.success("查询成功");
    }
}
