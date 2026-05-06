package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.ProductMapper;
import com.mumuwest.mumumike.mapper.ProductTypeMapper;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.ProductType;
import com.mumuwest.mumumike.pojo.VO.ProductVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
// 成dwadwa

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductTypeMapper productTypeMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductVOListOnlyQueriesEnabledTypesAndAvailableProducts() {
        ProductType enabledType = new ProductType();
        enabledType.setId(5);
        enabledType.setTitle("奶茶");
        enabledType.setStatus(1);
        Product product = new Product();
        product.setId(9);
        product.setProductTypeId(5);
        product.setStatus(1);

        when(productTypeMapper.getProductTypeList(any(ProductType.class))).thenReturn(List.of(enabledType));
        when(productMapper.getProductList(any(Product.class))).thenReturn(List.of(product));

        List<ProductVO> result = productService.getProductVOList();

        ArgumentCaptor<ProductType> typeCaptor = ArgumentCaptor.forClass(ProductType.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productTypeMapper).getProductTypeList(typeCaptor.capture());
        verify(productMapper).getProductList(productCaptor.capture());
        assertThat(typeCaptor.getValue().getStatus()).isEqualTo(1);
        assertThat(productCaptor.getValue().getProductTypeId()).isEqualTo(5);
        assertThat(productCaptor.getValue().getStatus()).isEqualTo(1);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductList()).containsExactly(product);
    }
}
