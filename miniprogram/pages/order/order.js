// pages/order/order.js - 订单列表页

const app = getApp();
const { orderApi } = require('../../utils/api');
const { formatTime, getOrderStatusText } = require('../../utils/util');

Page({
    data: {
        currentTab: 0, // 0: 进行中, 1: 已完成
        orderList: [],
        refreshing: false
    },

    onLoad() {
        this.loadOrders();
    },

    onShow() {
        this.loadOrders();

        // 设置tabbar选中状态
        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
            this.getTabBar().setData({ selected: 1 });
        }
    },

    // 切换标签
    switchTab(e) {
        const tab = parseInt(e.currentTarget.dataset.tab);
        if (tab !== this.data.currentTab) {
            this.setData({ currentTab: tab });
            this.loadOrders();
        }
    },

    // 加载订单列表
    async loadOrders() {
        const { currentTab } = this.data;

        // 检查登录状态
        if (!app.globalData.isLoggedIn) {
            this.setData({ orderList: [] });
            return;
        }

        wx.showLoading({ title: '加载中...' });

        try {
            const res = await orderApi.getOrderList({
                userId: app.globalData.userInfo?.id
            });

            // 兼容两种返回格式
            const orders = res.rows || res.data || [];

            // 根据tab过滤
            let filteredOrders;
            if (currentTab === 0) {
                filteredOrders = orders.filter(order => order.status !== 2 && order.status !== 3);
            } else {
                filteredOrders = orders.filter(order => order.status === 2);
            }

            // 处理订单数据
            const baseUrl = app.globalData.baseUrl;
            const orderList = filteredOrders.map(order => {
                // 计算总价
                let totalPrice = 0;
                // 处理产品数据，拼接图片URL
                const processedProducts = (order.product || []).map(item => {
                    totalPrice += (item.product?.price || 0) * (item.count || 0);
                    // 拼接图片URL
                    if (item.product && item.product.pictureUrl && !item.product.pictureUrl.startsWith('http')) {
                        item.product.pictureUrl = baseUrl + item.product.pictureUrl;
                    }
                    return item;
                });

                return {
                    ...order,
                    product: processedProducts,
                    statusText: getOrderStatusText(order.status),
                    createTimeText: formatTime(order.createTime, 'MM-DD HH:mm'),
                    totalPrice: totalPrice.toFixed(2)
                };
            });

            // 按时间倒序
            orderList.sort((a, b) => new Date(b.createTime) - new Date(a.createTime));

            this.setData({ orderList });
        } catch (err) {
            console.error('加载订单失败', err);
            wx.showToast({
                title: '加载失败',
                icon: 'none'
            });
        } finally {
            wx.hideLoading();
            this.setData({ refreshing: false });
        }
    },

    // 下拉刷新
    onRefresh() {
        this.setData({ refreshing: true });
        this.loadOrders();
    },

    // 跳转到订单详情
    goToDetail(e) {
        const id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `/pages/orderDetail/orderDetail?id=${id}`
        });
    },

    // 跳转到评价
    goToReview(e) {
        const id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `/pages/review/review?orderId=${id}`
        });
    },

    // 去点餐
    goToMenu() {
        wx.switchTab({
            url: '/pages/menu/menu'
        });
    }
});
