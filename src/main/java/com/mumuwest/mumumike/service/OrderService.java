package com.mumuwest.mumumike.service;

import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.pojo.OrderProduct;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     * @param order
     * @return
     */
    public int insertOrder(Order order);

    /**
     * 删除订单
     * @param id
     * @return
     */
    public int deleteOrder(Integer id);

    /**
     * 修改订单
     * @param order
     * @return
     */
    public int updateOrder(Order order);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    public Order getOrderById(Integer id);

    /**
     * 查询订单列表
     * @param order
     * @return
     */
    public List<Order> getOrderList(Order order);

    /**
     * 修改订单产品信息
     * @param orderProduct
     * @return
     */
    public int updateOrderProduct(OrderProduct orderProduct);
}
