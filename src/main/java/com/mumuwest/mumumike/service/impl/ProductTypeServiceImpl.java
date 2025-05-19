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
        return productTypeMapper.insertProductType(productType);
    }

    /**
     * 删除产品类型
     * @param productType
     * @return
     */
    @Override
    public int deleteProductType(ProductType productType) {
        return productTypeMapper.deleteProductType(productType);
    }

    /**
     * 更新产品类型
     * @param productType
     * @return
     */
    @Override
    public int updateProductType(ProductType productType) {
        return productTypeMapper.updateProductType(productType);
    }

    /**
     * 根据id查询产品类型
     * @param id
     * @return
     */
    @Override
    public ProductType getProductTypeById(int id) {
        return productTypeMapper.getProductTypeById(id);
    }

    /**
     * 获取ProductType列表
     * @param productType
     * @return
     */
    @Override
    public List<ProductType> getProductTypeList(ProductType productType) {
        return productTypeMapper.getProductTypeList(productType);
    }


}
