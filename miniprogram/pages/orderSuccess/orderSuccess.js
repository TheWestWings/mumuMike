// pages/orderSuccess/orderSuccess.js - 订单提交成功页

const app = getApp();
const { formatTime } = require('../../utils/util');

Page({
  data: {
    orderId: '',
    tableNo: '',
    totalPrice: '0.00',
    orderTime: '',
    paymentStatus: 'none', // none, pending, success
    paymentStatusText: ''
  },

  onLoad(options) {
    const orderId = options.orderId || '';
    const totalPrice = options.totalPrice || '0.00';
    const tableNo = app.globalData.tableNo || wx.getStorageSync('tableNo') || '';
    const orderTime = formatTime(new Date(), 'YYYY-MM-DD HH:mm:ss');

    this.setData({
      orderId,
      totalPrice,
      tableNo,
      orderTime,
      paymentStatus: 'pending',
      paymentStatusText: '请至收银台完成支付'
    });
  },

  // 查看订单
  viewOrder() {
    const { orderId } = this.data;
    if (orderId) {
      wx.redirectTo({
        url: `/pages/orderDetail/orderDetail?id=${orderId}`
      });
    } else {
      wx.switchTab({
        url: '/pages/order/order'
      });
    }
  },

  // 继续点餐
  continueShopping() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 发起微信支付
  async wxPay() {
    const { orderId, totalPrice } = this.data;
    
    wx.showLoading({ title: '正在支付...' });

    try {
      // 调用后端获取支付参数
      const res = await new Promise((resolve, reject) => {
        wx.request({
          url: `${app.globalData.baseUrl}/wx/pay/create`,
          method: 'POST',
          header: {
            'Authorization': 'Bearer ' + app.globalData.token
          },
          data: {
            orderId: orderId,
            totalFee: Math.round(parseFloat(totalPrice) * 100), // 转换为分
            body: '木木奶茶-订单支付'
          },
          success: (res) => resolve(res.data),
          fail: reject
        });
      });

      wx.hideLoading();

      if (res.code === 200 && res.data) {
        // 调起微信支付
        const payParams = res.data;
        wx.requestPayment({
          timeStamp: payParams.timeStamp,
          nonceStr: payParams.nonceStr,
          package: payParams.package,
          signType: payParams.signType || 'MD5',
          paySign: payParams.paySign,
          success: () => {
            this.setData({
              paymentStatus: 'success',
              paymentStatusText: '支付成功'
            });
            wx.showToast({
              title: '支付成功',
              icon: 'success'
            });
          },
          fail: (err) => {
            if (err.errMsg.indexOf('cancel') === -1) {
              wx.showToast({
                title: '支付失败',
                icon: 'none'
              });
            }
          }
        });
      } else {
        wx.showToast({
          title: res.msg || '获取支付参数失败',
          icon: 'none'
        });
      }
    } catch (err) {
      wx.hideLoading();
      console.error('支付失败', err);
      wx.showToast({
        title: '支付失败，请重试',
        icon: 'none'
      });
    }
  }
});
