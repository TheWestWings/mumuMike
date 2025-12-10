// utils/cart.js - 购物车管理

const app = getApp();

/**
 * 获取购物车
 * @returns {Array}
 */
const getCart = () => {
  return app.globalData.cart || [];
};

/**
 * 获取购物车商品数量
 * @returns {number}
 */
const getCartCount = () => {
  const cart = getCart();
  return cart.reduce((total, item) => total + item.count, 0);
};

/**
 * 获取购物车总价
 * @returns {number}
 */
const getCartTotal = () => {
  const cart = getCart();
  return cart.reduce((total, item) => total + (item.price * item.count), 0);
};

/**
 * 添加商品到购物车
 * @param {Object} product 商品信息
 * @param {number} count 数量
 * @param {string} comment 备注
 */
const addToCart = (product, count = 1, comment = '') => {
  const cart = getCart();
  const existIndex = cart.findIndex(item => 
    item.productId === product.id && item.comment === comment
  );

  if (existIndex > -1) {
    cart[existIndex].count += count;
  } else {
    cart.push({
      productId: product.id,
      name: product.name,
      price: product.price,
      pictureUrl: product.pictureUrl,
      description: product.description,
      count: count,
      comment: comment
    });
  }

  app.globalData.cart = cart;
  app.globalData.cartTotal = getCartTotal();
  saveCartToStorage();
  return cart;
};

/**
 * 从购物车移除商品
 * @param {number} index 商品索引
 */
const removeFromCart = (index) => {
  const cart = getCart();
  if (index >= 0 && index < cart.length) {
    cart.splice(index, 1);
    app.globalData.cart = cart;
    app.globalData.cartTotal = getCartTotal();
    saveCartToStorage();
  }
  return cart;
};

/**
 * 更新购物车商品数量
 * @param {number} index 商品索引
 * @param {number} count 新数量
 */
const updateCartItemCount = (index, count) => {
  const cart = getCart();
  if (index >= 0 && index < cart.length) {
    if (count <= 0) {
      cart.splice(index, 1);
    } else {
      cart[index].count = count;
    }
    app.globalData.cart = cart;
    app.globalData.cartTotal = getCartTotal();
    saveCartToStorage();
  }
  return cart;
};

/**
 * 更新购物车商品备注
 * @param {number} index 商品索引
 * @param {string} comment 备注
 */
const updateCartItemComment = (index, comment) => {
  const cart = getCart();
  if (index >= 0 && index < cart.length) {
    cart[index].comment = comment;
    app.globalData.cart = cart;
    saveCartToStorage();
  }
  return cart;
};

/**
 * 清空购物车
 */
const clearCart = () => {
  app.globalData.cart = [];
  app.globalData.cartTotal = 0;
  wx.removeStorageSync('cart');
  return [];
};

/**
 * 保存购物车到本地存储
 */
const saveCartToStorage = () => {
  wx.setStorageSync('cart', app.globalData.cart);
};

/**
 * 从本地存储加载购物车
 */
const loadCartFromStorage = () => {
  const cart = wx.getStorageSync('cart') || [];
  app.globalData.cart = cart;
  app.globalData.cartTotal = getCartTotal();
  return cart;
};

/**
 * 根据商品ID获取购物车中的数量
 * @param {number} productId 商品ID
 * @returns {number}
 */
const getCartItemCount = (productId) => {
  const cart = getCart();
  const items = cart.filter(item => item.productId === productId);
  return items.reduce((total, item) => total + item.count, 0);
};

module.exports = {
  getCart,
  getCartCount,
  getCartTotal,
  addToCart,
  removeFromCart,
  updateCartItemCount,
  updateCartItemComment,
  clearCart,
  saveCartToStorage,
  loadCartFromStorage,
  getCartItemCount
};
