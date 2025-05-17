package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.ProductMapper;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    public ProductMapper productMapper;

    @Override
    public int insertProduct(Product product) {
        return productMapper.insertProduct(product);
    }

    @Override
    public int updateProduct(Product product) {
        return productMapper.updateProduct(product);
    }

    @Override
    public int deleteProduct(Integer id) {
        return productMapper.deleteProduct(id);
    }

    @Override
    public Product getProductById(Integer id) {
        return productMapper.getProductById(id);
    }

    @Override
    public List<Product> getProductList(Product product) {
        return productMapper.getProductList(product);
    }
}
