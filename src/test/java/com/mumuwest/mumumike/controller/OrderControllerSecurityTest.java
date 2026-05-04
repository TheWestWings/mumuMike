package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.pojo.OrderProduct;
import com.mumuwest.mumumike.pojo.User;
import com.mumuwest.mumumike.service.OrderService;
import com.mumuwest.mumumike.service.UserService;
import com.mumuwest.mumumike.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerSecurityTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Test
    void updateOrderProductRequiresAdminOrMerchantRole() throws Exception {
        Method method = OrderController.class.getDeclaredMethod("updateOrderProduct", OrderProduct.class);
        Role role = method.getAnnotation(Role.class);

        assertThat(role).isNotNull();
        assertThat(role.role()).containsExactly(0, 1);
    }

    @Test
    void insertOrderUsesAuthenticatedUserInsteadOfRequestBodyUserId() {
        OrderController controller = new OrderController();
        ReflectionTestUtils.setField(controller, "orderService", orderService);
        ReflectionTestUtils.setField(controller, "userService", userService);
        User currentUser = new User();
        currentUser.setId(7);
        currentUser.setUsername("customer");
        currentUser.setRole(2);
        when(userService.getUserByUsername("customer")).thenReturn(currentUser);
        when(orderService.insertOrder(any(Order.class))).thenReturn(100);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + JwtUtil.generateToken("customer", 2));
        Order order = new Order();
        order.setUserId(999);
        order.setProduct(List.of());

        AjaxResult result = controller.insertOrder(order, request);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).insertOrder(captor.capture());
        assertThat(result.get("code")).isEqualTo(200);
        assertThat(captor.getValue().getUserId()).isEqualTo(7);
    }
}
