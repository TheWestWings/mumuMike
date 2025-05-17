package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.ProductType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductTypeMapper {
    /**
     * 新建产品类型
     * @param productType
     * @return
     */
    public int insertProductType(ProductType productType);

    /**
     * 更新产品类型
     * @param productType
     * @return
     */
    public int deleteProductType(ProductType productType);

    /**
     * 更新产品类型
     * @param productType
     * @return
     */
    public int updateProductType(ProductType productType);

    /**
     * 根据id查询产品类型
     * @param id
     * @return
     */
    public ProductType getProductTypeById(int id);

    /**
     * 获取ProductType列表
     * @param productType
     * @return
     */
    public List<ProductType> getProductTypeList(ProductType productType);
}
