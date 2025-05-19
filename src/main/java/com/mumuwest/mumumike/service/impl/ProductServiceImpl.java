package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.ProductMapper;
import com.mumuwest.mumumike.mapper.ProductTypeMapper;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.ProductType;
import com.mumuwest.mumumike.pojo.VO.ProductVO;
import com.mumuwest.mumumike.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    public ProductMapper productMapper;
    @Autowired
    public ProductTypeMapper productTypeMapper;


    /**
     * 添加产品
     * @param product
     * @return
     */
    @Override
    public int insertProduct(Product product) {
        return productMapper.insertProduct(product);
    }

    /**
     * 更新产品
     * @param product
     * @return
     */
    @Override
    public int updateProduct(Product product) {
        return productMapper.updateProduct(product);
    }

    /**
     * 删除产品
     * @param id
     * @return
     */
    @Override
    public int deleteProduct(Integer id) {
        return productMapper.deleteProduct(id);
    }

    /**
     * 根据id查询产品
     * @param id
     * @return
     */
    @Override
    public Product getProductById(Integer id) {
        Product product = productMapper.getProductById(id);
        if(product != null) {
            ProductType productType = productTypeMapper.getProductTypeById(product.getProductTypeId());
            product.setProductTypeName(productType.getTitle());
        }
        return product;
    }

    /**
     * 查询产品列表
     * @param product
     * @return
     */
    @Override
    public List<Product> getProductList(Product product) {
        List<Product> productList = productMapper.getProductList(product);
        for(Product productItem : productList) {
            ProductType productType = productTypeMapper.getProductTypeById(productItem.getProductTypeId());
            productItem.setProductTypeName(productType.getTitle());
        }
        return productList;
    }

    /**
     * 查询产品VO列表
     * @return List<ProductVO>
     */
    @Override
    public List<ProductVO> getProductVOList() {
        ProductType productTypeQuery = new ProductType();
        productTypeQuery.setStatus(1);
        List<ProductType> productTypes = productTypeMapper.getProductTypeList(productTypeQuery);
        List<ProductVO> productVOList = new ArrayList<>();
        for(ProductType productType : productTypes) {
            ProductVO productVO = new ProductVO();
            productVO.setId(productType.getId());
            productVO.setTitle(productType.getTitle());
            productVO.setDescription(productType.getDescription());
            productVO.setStatus(productType.getStatus());
            Product product = new Product();
            product.setProductTypeId(productType.getId());
            List<Product> productList = productMapper.getProductList(product);
            productVO.setProductList(productList);
            productVOList.add(productVO);
        }

        return productVOList;
    }
}
