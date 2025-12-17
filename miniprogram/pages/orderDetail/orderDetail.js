// pages/orderDetail/orderDetail.js - 订单详情页

const app = getApp();
const { orderApi } = require('../../utils/api');
const { formatTime, getOrderStatusText } = require('../../utils/util');

Page({
  data: {
    orderId: null,
    order: {},
    productList: [],
    statusText: '',
    createTimeText: '',
    updateTimeText: '',
    totalPrice: '0.00',
    tableNo: ''
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id });
      this.loadOrderDetail(options.id);
    }
  },

  // 加载订单详情
  async loadOrderDetail(id) {
    wx.showLoading({ title: '加载中...' });

    try {
      const res = await orderApi.getOrderById(id);
      console.log('API Response:', res); // Debug log
      
      // 兼容两种返回格式: 直接返回对象 或 {code, data}
      let order = null;
      if (res.data) {
        order = res.data;
      } else if (res.id || res.message) { // 假设直接返回了对象 (res.message check as fallback)
        order = res;
      }

      console.log('Parsed Order:', order); // Debug log

      if (order && (order.id || order.product)) {
        const productList = order.product || [];
        
        // 计算总价
        let totalPrice = 0;
        productList.forEach(item => {
          if (item.product && item.product.status !== 2) { // 非退单商品
             // Ensure price and count are numbers
            const price = parseFloat(item.product?.price || 0);
            const count = parseInt(item.count || 0);
            totalPrice += price * count;
          }
        });
        
        // Ensure status is a number for consistent checks
        const status = typeof order.status !== 'undefined' ? parseInt(order.status) : -1;

        this.setData({
          order: {
            ...order,
            status: status // Overwrite status with parsed number
          },
          productList: productList,
          statusText: getOrderStatusText(status),
          createTimeText: formatTime(order.createTime, 'YYYY-MM-DD HH:mm:ss'),
          updateTimeText: order.updateTime ? formatTime(order.updateTime, 'YYYY-MM-DD HH:mm:ss') : '',
          totalPrice: totalPrice.toFixed(2),
          tableNo: app.globalData.tableNo || ''
        });
      } else {
        console.warn('Order data invalid or empty:', order);
        wx.showToast({
          title: '订单数据异常',
          icon: 'none'
        });
      }
    } catch (err) {
      console.error('加载订单详情失败', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 去评价
  goToReview() {
    wx.navigateTo({
      url: `/pages/review/review?orderId=${this.data.orderId}`
    });
  },

  // 分享
  onShareAppMessage() {
    return {
      title: '木木奶茶 - 我的订单',
      path: '/pages/index/index'
    };
  }
});
