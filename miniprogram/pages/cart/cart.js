// pages/cart/cart.js - 购物车页面

const app = getApp();
const cartUtil = require('../../utils/cart');
const { orderApi } = require('../../utils/api');

Page({
  data: {
    cartList: [], // 购物车列表
    cartCount: 0, // 商品总数
    cartTotal: 0, // 总价
    tableNo: '', // 桌号
    submitting: false, // 提交中
    showRemarkModal: false, // 显示备注弹窗
    editingIndex: -1, // 正在编辑的商品索引
    editingRemark: '' // 正在编辑的备注
  },

  onLoad() {
    this.loadCart();
  },

  onShow() {
    this.loadCart();
    const tableNo = app.globalData.tableNo || wx.getStorageSync('tableNo');
    this.setData({ tableNo });
  },

  // 加载购物车
  loadCart() {
    cartUtil.loadCartFromStorage();
    const cartList = cartUtil.getCart();
    const cartCount = cartUtil.getCartCount();
    const cartTotal = cartUtil.getCartTotal().toFixed(2);
    
    this.setData({ cartList, cartCount, cartTotal });
  },

  // 返回
  goBack() {
    wx.navigateBack();
  },

  // 清空购物车
  clearCart() {
    wx.showModal({
      title: '提示',
      content: '确定要清空购物车吗？',
      success: (res) => {
        if (res.confirm) {
          cartUtil.clearCart();
          this.loadCart();
        }
      }
    });
  },

  // 增加商品
  increaseItem(e) {
    const index = e.currentTarget.dataset.index;
    const cart = cartUtil.getCart();
    const newCount = cart[index].count + 1;
    cartUtil.updateCartItemCount(index, newCount);
    this.loadCart();
    wx.vibrateShort({ type: 'light' });
  },

  // 减少商品
  decreaseItem(e) {
    const index = e.currentTarget.dataset.index;
    const cart = cartUtil.getCart();
    const currentCount = cart[index].count;
    
    if (currentCount === 1) {
      wx.showModal({
        title: '提示',
        content: '确定要删除这个商品吗？',
        success: (res) => {
          if (res.confirm) {
            cartUtil.updateCartItemCount(index, 0);
            this.loadCart();
          }
        }
      });
    } else {
      cartUtil.updateCartItemCount(index, currentCount - 1);
      this.loadCart();
      wx.vibrateShort({ type: 'light' });
    }
  },

  // 编辑备注
  editRemark(e) {
    const index = e.currentTarget.dataset.index;
    const cart = cartUtil.getCart();
    this.setData({
      showRemarkModal: true,
      editingIndex: index,
      editingRemark: cart[index].comment || ''
    });
  },

  // 关闭备注弹窗
  closeRemarkModal() {
    this.setData({
      showRemarkModal: false,
      editingIndex: -1,
      editingRemark: ''
    });
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({ editingRemark: e.detail.value });
  },

  // 保存备注
  saveRemark() {
    const { editingIndex, editingRemark } = this.data;
    if (editingIndex >= 0) {
      cartUtil.updateCartItemComment(editingIndex, editingRemark);
      this.loadCart();
    }
    this.closeRemarkModal();
  },

  // 提交订单
  async submitOrder() {
    const { cartList, tableNo } = this.data;
    
    if (cartList.length === 0) {
      wx.showToast({
        title: '购物车是空的',
        icon: 'none'
      });
      return;
    }

    // 检查登录状态
    if (!app.globalData.isLoggedIn) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再下单',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        }
      });
      return;
    }

    this.setData({ submitting: true });

    try {
      // 构建订单数据
      const orderData = {
        userId: app.globalData.userInfo?.id,
        tableNo: tableNo,
        product: cartList.map(item => ({
          productId: item.productId,
          count: item.count,
          comment: item.comment || ''
        })),
        createTime: new Date().toISOString()
      };

      const res = await orderApi.submitOrder(orderData);
      
      if (res.code === 200 || res.code === 0) {
        // 清空购物车
        cartUtil.clearCart();
        
        // 获取订单ID和总价
        const orderId = res.data?.id || res.data || '';
        const totalPrice = this.data.cartTotal;

        wx.showToast({
          title: '下单成功！',
          icon: 'success'
        });

        // 跳转到订单成功页面
        setTimeout(() => {
          wx.redirectTo({
            url: `/pages/orderSuccess/orderSuccess?orderId=${orderId}&totalPrice=${totalPrice}`
          });
        }, 1000);
      }
    } catch (err) {
      console.error('提交订单失败', err);
      wx.showToast({
        title: '下单失败，请重试',
        icon: 'none'
      });
    } finally {
      this.setData({ submitting: false });
    }
  },

  // 去点餐
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  }
});
