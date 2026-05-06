package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.ProductType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeControllerSecurityTest {

    @Test
    void updateProductTypeStatusRequiresAdminOrMerchantRole() throws Exception {
        Method method = ProductTypeController.class.getDeclaredMethod("updateProductTypeStatus", ProductType.class);

        Role role = method.getAnnotation(Role.class);

        assertThat(role).isNotNull();
        assertThat(role.role()).containsExactly(3);
    }
}
