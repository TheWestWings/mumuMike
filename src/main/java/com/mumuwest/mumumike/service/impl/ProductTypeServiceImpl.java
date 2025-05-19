package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.ProductTypeMapper;
import com.mumuwest.mumumike.pojo.ProductType;
import com.mumuwest.mumumike.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    /**
     * 新建产品类型
     * @param productType
     * @return
     */
    @Override
    public int insertProductType(ProductType productType) {
        productType.setStatus(1);
        return productTypeMapper.insertProductType(productType);
    }

    @Override
    public int deleteProductType(ProductType productType) {
        return productTypeMapper.deleteProductType(productType);
    }

    @Override
    public int updateProductType(ProductType productType) {
        return productTypeMapper.updateProductType(productType);
    }

    @Override
    public ProductType getProductTypeById(int id) {
        return productTypeMapper.getProductTypeById(id);
    }

    @Override
    public List<ProductType> getProductTypeList(ProductType productType) {
        return productTypeMapper.getProductTypeList(productType);
    }


}
