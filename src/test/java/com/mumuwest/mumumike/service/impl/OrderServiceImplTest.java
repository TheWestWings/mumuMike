package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.OrderMapper;
import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.pojo.OrderProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void insertOrderBindsGeneratedOrderIdToProducts() {
        doAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(88);
            return 1;
        }).when(orderMapper).insertOrder(any(Order.class));

        OrderProduct first = new OrderProduct();
        first.setProductId(11);
        first.setCount(2);
        OrderProduct second = new OrderProduct();
        second.setProductId(12);
        second.setCount(1);
        Order order = new Order();
        order.setUserId(7);
        order.setProduct(List.of(first, second));

        int orderId = orderService.insertOrder(order);

        assertThat(orderId).isEqualTo(88);
        assertThat(first.getOrderId()).isEqualTo(88);
        assertThat(second.getOrderId()).isEqualTo(88);
        verify(orderMapper).insertOrderProduct(first);
        verify(orderMapper).insertOrderProduct(second);
    }
}
