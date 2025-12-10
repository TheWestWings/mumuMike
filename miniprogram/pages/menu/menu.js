// pages/menu/menu.js - 菜单点餐页

const app = getApp();
const { productApi } = require('../../utils/api');
const cartUtil = require('../../utils/cart');

Page({
  data: {
    categoryList: [], // 分类及商品列表
    currentCategory: 0, // 当前选中分类索引
    scrollToView: '', // 滚动到指定分类
    cartCount: 0, // 购物车商品数量
    cartTotal: 0, // 购物车总价
    cartCountMap: {}, // 商品ID对应的购物车数量
    tableNo: '', // 桌号
    showSpecModal: false, // 规格弹窗
    currentProduct: null, // 当前选中商品
    productRemark: '' // 商品备注
  },

  onLoad() {
    // 加载购物车
    cartUtil.loadCartFromStorage();
    this.loadProductList();
  },

  onShow() {
    // 更新购物车状态
    this.updateCartStatus();
    
    // 获取桌号
    const tableNo = app.globalData.tableNo || wx.getStorageSync('tableNo');
    this.setData({ tableNo });
    
    // 设置tabbar选中状态
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 0 });
    }
  },

  // 加载商品列表
  async loadProductList() {
    wx.showLoading({ title: '加载中...' });
    try {
      const res = await productApi.getProductVOList();
      // 兼容两种返回格式
      const data = res.rows || res.data || [];
      const baseUrl = app.globalData.baseUrl;
      
      // 过滤掉下架的分类和商品，并处理图片URL
      const categoryList = data
        .filter(category => category.status !== 0)
        .map(category => ({
          ...category,
          productList: (category.productList || [])
            .filter(product => product.status !== 0)
            .map(product => ({
              ...product,
              // 如果pictureUrl是相对路径，拼接baseUrl
              pictureUrl: product.pictureUrl && !product.pictureUrl.startsWith('http') 
                ? baseUrl + product.pictureUrl 
                : product.pictureUrl
            }))
        }))
        .filter(category => category.productList.length > 0);
      
      this.setData({ categoryList });
      this.updateCartCountMap();
    } catch (err) {
      console.error('加载商品列表失败', err);
      wx.showToast({
        title: '加载失败，请重试',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 切换分类
  switchCategory(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      currentCategory: index,
      scrollToView: `category-${index}`
    });
  },

  // 商品列表滚动监听
  onProductScroll(e) {
    // 可以根据滚动位置自动切换左侧分类高亮
    // 此处简化处理
  },

  // 增加商品数量
  increaseCount(e) {
    const product = e.currentTarget.dataset.product;
    cartUtil.addToCart(product, 1);
    this.updateCartStatus();
    this.updateCartCountMap();
    
    // 轻微震动反馈
    wx.vibrateShort({ type: 'light' });
  },

  // 减少商品数量
  decreaseCount(e) {
    const product = e.currentTarget.dataset.product;
    const cart = cartUtil.getCart();
    const index = cart.findIndex(item => item.productId === product.id);
    
    if (index > -1) {
      const newCount = cart[index].count - 1;
      cartUtil.updateCartItemCount(index, newCount);
      this.updateCartStatus();
      this.updateCartCountMap();
      
      wx.vibrateShort({ type: 'light' });
    }
  },

  // 更新购物车状态
  updateCartStatus() {
    const cartCount = cartUtil.getCartCount();
    const cartTotal = cartUtil.getCartTotal().toFixed(2);
    this.setData({ cartCount, cartTotal });
  },

  // 更新商品数量映射
  updateCartCountMap() {
    const cart = cartUtil.getCart();
    const cartCountMap = {};
    cart.forEach(item => {
      cartCountMap[item.productId] = (cartCountMap[item.productId] || 0) + item.count;
    });
    this.setData({ cartCountMap });
  },

  // 显示规格弹窗
  showSpecModal(e) {
    const product = e.currentTarget.dataset.product;
    this.setData({
      showSpecModal: true,
      currentProduct: product,
      productRemark: ''
    });
  },

  // 关闭规格弹窗
  closeSpecModal() {
    this.setData({
      showSpecModal: false,
      currentProduct: null,
      productRemark: ''
    });
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({ productRemark: e.detail.value });
  },

  // 确认加入购物车
  confirmAddToCart() {
    const { currentProduct, productRemark } = this.data;
    if (currentProduct) {
      cartUtil.addToCart(currentProduct, 1, productRemark);
      this.updateCartStatus();
      this.updateCartCountMap();
      this.closeSpecModal();
      
      wx.showToast({
        title: '已加入购物车',
        icon: 'success'
      });
    }
  },

  // 跳转到购物车
  goToCart() {
    if (this.data.cartCount > 0) {
      wx.navigateTo({
        url: '/pages/cart/cart'
      });
    }
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadProductList().then(() => {
      wx.stopPullDownRefresh();
    });
  }
});
