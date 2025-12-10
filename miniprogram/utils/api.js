// utils/api.js - API接口封装

const { get, post, put, del } = require('./request');

// 用户相关
const userApi = {
  // 登录
  login: (data) => post('/login', data),
  // 微信登录
  wxLogin: (data) => post('/wx/login', data),
  // 注册
  register: (data) => post('/register', data),
  // 获取用户信息
  getInfo: () => get('/getInfo'),
  // 更新用户信息
  updateUser: (data) => put('/updateUser', data),
};

// 商品相关
const productApi = {
  // 获取商品列表（包含分类）
  getProductVOList: () => get('/product/getProductVOList'),
  // 获取商品详情
  getProductById: (id) => get(`/product/${id}`),
  // 获取商品列表
  getProductList: (data) => post('/product/getProductList', data),
};

// 分类相关
const categoryApi = {
  // 获取分类列表
  getProductTypeList: (data) => post('/productType/getProductTypeList', data),
};

// 订单相关
const orderApi = {
  // 创建订单
  submitOrder: (data) => post('/order', data),
  // 获取订单列表
  getOrderList: (data) => post('/order/getListVO', data),
  // 获取订单详情
  getOrderById: (id) => get(`/order/getOrderVOById/${id}`),
  // 更新订单状态
  updateOrderStatus: (data) => put('/order/updateOrderStatus', data),
  // 更新订单商品状态
  updateOrderProduct: (data) => put('/order/updateOrderProduct', data),
};

// 消息相关
const messageApi = {
  // 获取消息列表
  getMessageList: (userId) => get(`/message/getMessageListByUserId/${userId}`),
  // 更新消息状态
  updateMessage: (data) => put('/message', data),
};

// 反馈/评价相关
const informationApi = {
  // 提交反馈
  addInformation: (data) => post('/information', data),
};

module.exports = {
  userApi,
  productApi,
  categoryApi,
  orderApi,
  messageApi,
  informationApi
};
