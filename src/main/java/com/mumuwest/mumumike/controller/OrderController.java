package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.*;
import com.mumuwest.mumumike.pojo.VO.OrderProductVO;
import com.mumuwest.mumumike.pojo.VO.OrderVO;
import com.mumuwest.mumumike.service.MessageService;
import com.mumuwest.mumumike.service.OrderService;
import com.mumuwest.mumumike.service.ProductService;
import com.mumuwest.mumumike.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private MessageService messageService;

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
        if(orderProduct.getStatus() == 2){
            OrderProduct orderProductById = orderService.getOrderProductById(orderProduct.getId());
            Product product = productService.getProductById(orderProductById.getProductId());
            Order orderById = orderService.getOrderById(orderProductById.getOrderId());
            Message message = new Message();
            message.setUserId(orderById.getUserId());
            message.setTitle("订单已退单");
            message.setContent("订单号：" + orderById.getId() + "的商品已退单，商品名称：" + product.getName()  + " 退单原因：" + orderProduct.getComment());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = sdf.format(new Date());
            message.setCreateTime(formattedDateTime);
            messageService.createMessage(message);
        }
        return AjaxResult.success(orderService.updateOrderProduct(orderProduct));
    }

    @PutMapping("/updateOrderStatus")
    @Role(role = {0, 1})
    public AjaxResult updateOrderStatus(@RequestBody Order order) {
        Order orderUpdate = new Order();
        orderUpdate.setId(order.getId());
        orderUpdate.setStatus(order.getStatus());
        if(orderUpdate.getStatus() == 2){
            Order orderById = orderService.getOrderById(orderUpdate.getId());
            Message message = new Message();
            message.setUserId(orderById.getUserId());
            message.setTitle("订单已完成，请取餐");
            message.setContent("订单号：" + orderUpdate.getId() + "，请及时取餐");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = sdf.format(new Date());
            message.setCreateTime(formattedDateTime);
            messageService.createMessage(message);
            orderUpdate.setUpdateTime(new Date());
        }
        return AjaxResult.success(orderService.updateOrder(orderUpdate));
    }


}
