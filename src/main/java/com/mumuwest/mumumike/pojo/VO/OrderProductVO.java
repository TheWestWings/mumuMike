package com.mumuwest.mumumike.pojo.VO;


import com.mumuwest.mumumike.pojo.OrderProduct;
import com.mumuwest.mumumike.pojo.Product;

public class OrderProductVO {
    private Integer id;
    private Integer productId;
    private Product product;
    private Integer orderId;
    private Integer count;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public OrderProductVO(Product product, OrderProduct orderProduct) {
        this.id = orderProduct.getId();
        this.productId = orderProduct.getProductId();
        this.product = product;
        this.orderId = orderProduct.getOrderId();
        this.count = orderProduct.getCount();
        this.status = orderProduct.getStatus();
    }
}
