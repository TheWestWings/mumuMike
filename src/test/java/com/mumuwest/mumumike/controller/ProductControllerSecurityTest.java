package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ProductControllerSecurityTest {

    @Test
    void managementEndpointsRequireAdminOrMerchantRole() throws Exception {
        assertRole(ProductController.class.getDeclaredMethod("getProductList", Product.class));
        assertRole(ProductController.class.getDeclaredMethod("insertProduct", String.class, String.class,
                Integer.class, Integer.class, MultipartFile.class));
        assertRole(ProductController.class.getDeclaredMethod("updateProduct", String.class, Integer.class,
                String.class, Integer.class, Integer.class, MultipartFile.class));
        assertRole(ProductController.class.getDeclaredMethod("deleteProduct", Integer.class));
        assertRole(ProductController.class.getDeclaredMethod("updateProductStatus", Product.class));
    }

    private void assertRole(Method method) {
        Role role = method.getAnnotation(Role.class);
        assertThat(role).as(method.getName() + " should be protected by @Role").isNotNull();
        assertThat(role.role()).containsExactly(0, 1);
    }
}
