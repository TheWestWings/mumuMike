package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.*;
import com.mumuwest.mumumike.pojo.VO.OrderProductVO;
import com.mumuwest.mumumike.pojo.VO.OrderVO;
import com.mumuwest.mumumike.service.OrderService;
import com.mumuwest.mumumike.service.ProductService;
import com.mumuwest.mumumike.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 通过id查询订单
     * @param id
     * @return
     */
    @GetMapping("/getOrderById/{id}")
    @Role(role = {0, 1})
    public AjaxResult getOrderById(@PathVariable("id") Integer id) {
        return AjaxResult.success(orderService.getOrderById(id));
    }

    /**
     * 创建订单
     * @param order
     * @return
     */
    @GetMapping("/getList")
    @Role(role = {0, 1})
    public TableDataInfo getList(Order order) {
        List<Order> orderList = orderService.getOrderList(order);
        return new TableDataInfo(orderList, orderList.size());
    }

    /**
     * 获取VO
     * @param id
     * @return
     */
    @GetMapping("/getOrderVOById/{id}")
    @Role(role = {0, 1})
    public AjaxResult getOrderVOById(@PathVariable("id") Integer id) {
        Order orderById = orderService.getOrderById(id);
        User userById = userService.getUserById(orderById.getUserId());
        List<OrderProductVO> orderProductVOList = new ArrayList<>();
        for (OrderProduct orderProduct : orderById.getProduct()) {
            Product productById = productService.getProductById(orderProduct.getProductId());
            orderProductVOList.add(new OrderProductVO(productById, orderProduct));
        }
        OrderVO orderVO = new OrderVO(orderById, userById, orderProductVOList);
        return AjaxResult.success(orderVO);
    }


    /**
     * 获取VO列表
     * @param order
     * @return
     */
    @PostMapping("/getListVO")
    @Role(role = {0, 1})
    public TableDataInfo getListVO(@RequestBody Order order) {
        List<Order> orderList = orderService.getOrderList(order);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order orderItem : orderList) {
            User userById = userService.getUserById(orderItem.getUserId());
            List<OrderProductVO> orderProductVOList = new ArrayList<>();
            for (OrderProduct orderProduct : orderItem.getProduct()) {
                Product productById = productService.getProductById(orderProduct.getProductId());
                orderProductVOList.add(new OrderProductVO(productById, orderProduct));
            }
            OrderVO orderVO = new OrderVO(orderItem, userById, orderProductVOList);
            orderVOList.add(orderVO);
        }
        return new TableDataInfo(orderVOList, orderList.size());
    }

    /**
     * 创建订单
     * @param order
     * @return
     */
    @PostMapping
    public AjaxResult insertOrder(@RequestBody Order order) {
        return AjaxResult.success(orderService.insertOrder(order));
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Role(role = {0, 1})
    public AjaxResult deleteOrder(@PathVariable("id") Integer id) {
        return AjaxResult.success(orderService.deleteOrder(id));
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    @PutMapping
    @Role(role = {0, 1})
    public AjaxResult updateOrder(Order order) {
        return AjaxResult.success(orderService.updateOrder(order));
    }

    /**
     * 修改订单产品状态
     * @param orderProduct
     * @return
     */
    @PutMapping("/updateOrderProduct")
    public AjaxResult updateOrderProduct(@RequestBody OrderProduct orderProduct) {
        return AjaxResult.success(orderService.updateOrderProduct(orderProduct));
    }

    @PutMapping("/updateOrderStatus")
    @Role(role = {0, 1})
    public AjaxResult updateOrderStatus(@RequestBody Order order) {
        Order orderUpdate = new Order();
        orderUpdate.setId(order.getId());
        orderUpdate.setStatus(order.getStatus());
        return AjaxResult.success(orderService.updateOrder(orderUpdate));
    }


}
