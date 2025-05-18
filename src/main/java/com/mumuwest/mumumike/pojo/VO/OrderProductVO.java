package com.mumuwest.mumumike.pojo.VO;

import com.mumuwest.mumumike.pojo.Product;

public class OrderProductVO {
    // product
    private Integer id;
    private Integer productTypeId;
    private String name;
    private String description;
    private Integer price;
    private String pictureUrl;
    // add
    private Integer count;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OrderProductVO(Product product, Integer count, Integer status) {
        this.id = product.getId();
        this.productTypeId = product.getProductTypeId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.pictureUrl = product.getPictureUrl();
        this.count = count;
        this.status = status;
    }
}
