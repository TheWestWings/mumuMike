package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 新建订单
     * @param order
     * @return
     */
    public int insertOrder(Order order);

    /**
     * 修改订单
     * @param order
     * @return
     */
    public int updateOrder(Order order);

    /**
     * 删除订单
     * @param id
     * @return
     */
    public int deleteOrder(Integer id);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    public Order getOrderById(Integer id);

    /**
     * 查询全部订单
     * @param order
     * @return
     */
    public List<Order> getOrdersList(Order order);
}
