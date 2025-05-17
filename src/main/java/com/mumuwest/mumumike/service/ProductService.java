package com.mumuwest.mumumike.service;

import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.VO.ProductVO;

import java.util.List;

public interface ProductService {

    /**
     * 添加产品
     * @param product
     * @return
     */
    public int insertProduct(Product product);

    /**
     * 更新产品
     * @param product
     * @return
     */
    public int updateProduct(Product product);

    /**
     * 删除产品
     * @param id
     * @return
     */
    public int deleteProduct(Integer id);

    /**
     * 根据id查询产品
     * @param id
     * @return
     */
    public Product getProductById(Integer id);

    /**
     * 查询产品列表
     * @param product
     * @return
     */
    public List<Product> getProductList(Product product);

    /**
     * 查询产品VO列表
     * @return
     */
    public List<ProductVO> getProductVOList();
}
