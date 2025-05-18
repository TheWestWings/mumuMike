package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.OrderMapper;
import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int insertOrder(Order order) {
        return orderMapper.insertOrder(order);
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
}
