package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.OrderMapper;
import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.pojo.OrderProduct;
import com.mumuwest.mumumike.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    @Override
    public int insertOrder(Order order) {
        orderMapper.insertOrder(order);
        List<OrderProduct> orderProducts = order.getProduct();
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrderId(order.getId());
            orderMapper.insertOrderProduct(orderProduct);
        }
        return 1;
    }

    @Override
    public int deleteOrder(Integer id) {
        return orderMapper.deleteOrder(id);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateOrder(order);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.getOrderById(id);
    }

    @Override
    public List<Order> getOrderList(Order order) {
        return orderMapper.getOrdersList(order);
    }

    @Override
    public int updateOrderProduct(OrderProduct orderProduct) {
        return orderMapper.updateOrderProduct(orderProduct);
    }
}
