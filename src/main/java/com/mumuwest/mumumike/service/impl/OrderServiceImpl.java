package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.OrderMapper;
import com.mumuwest.mumumike.pojo.Message;
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

    /**
     * 创建订单
     * @param order
     * @return
     */
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

    /**
     * 删除订单
     * @param id
     * @return
     */
    @Override
    public int deleteOrder(Integer id) {
        return orderMapper.deleteOrder(id);
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateOrder(order);
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.getOrderById(id);
    }

    @Override
    public OrderProduct getOrderProductById(Integer id) {
        return orderMapper.getOrderProductById(id);
    }

    /**
     * 查询订单列表
     * @param order
     * @return
     */
    @Override
    public List<Order> getOrderList(Order order) {
        return orderMapper.getOrdersList(order);
    }

    /**
     * 修改订单产品信息
     * @param orderProduct
     * @return
     */
    @Override
    public int updateOrderProduct(OrderProduct orderProduct) {
        return orderMapper.updateOrderProduct(orderProduct);
    }
}
