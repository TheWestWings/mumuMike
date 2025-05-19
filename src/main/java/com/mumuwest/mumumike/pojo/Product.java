package com.mumuwest.mumumike.pojo;

public class Product {
    private Integer id;
    private Integer productTypeId;
    private String name;
    private String description;
    private Integer price;
    private Integer status; // 1: 上架 0：下架
    private String pictureUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        String baseUrl = "http://localhost:8080";
        String relativePath = pictureUrl.replace("./", "");
        this.pictureUrl = baseUrl + "/" + relativePath;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
