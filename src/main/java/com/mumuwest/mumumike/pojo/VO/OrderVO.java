package com.mumuwest.mumumike.pojo.VO;

import com.mumuwest.mumumike.pojo.Order;
import com.mumuwest.mumumike.pojo.OrderProduct;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.User;

import java.util.Date;
import java.util.List;

public class OrderVO {
    private Integer id;
    private Integer userId;
    private List<OrderProductVO> product;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<OrderProductVO> getProduct() {
        return product;
    }

    public void setProduct(List<OrderProductVO> product) {
        this.product = product;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderVO(Order order, User user, List<OrderProductVO> orderProductVOs) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.product = orderProductVOs;
        this.status = order.getStatus();
        this.createTime = order.getCreateTime();
        this.updateTime = order.getUpdateTime();
        this.user = user;
    }
}
