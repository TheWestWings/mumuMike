// utils/request.js - HTTP请求封装

const app = getApp();

/**
 * 发起HTTP请求
 * @param {Object} options 请求配置
 * @returns {Promise}
 */
const request = (options) => {
  return new Promise((resolve, reject) => {
    const header = {
      'Content-Type': options.contentType || 'application/json',
      ...options.header
    };

    // 如果有token，添加到header
    if (app.globalData.token) {
      header['Authorization'] = 'Bearer ' + app.globalData.token;
    }

    wx.request({
      url: app.globalData.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: header,
      success(res) {
        if (res.statusCode === 200) {
          // 兼容两种返回格式：
          // 1. 标准格式: {code: 200, data: ..., msg: ...}
          // 2. 分页格式: {code: 0, total: ..., rows: [...], msg: ...}
          const data = res.data;
          const isSuccess = data.code === 200 || data.code === 0 || data.code === undefined;
          
          if (isSuccess) {
            resolve(data);
          } else if (data.code === 401) {
            // token过期，清除登录状态
            app.globalData.token = '';
            app.globalData.isLoggedIn = false;
            wx.removeStorageSync('token');
            wx.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            });
            reject(data);
          } else {
            wx.showToast({
              title: data.msg || '请求失败',
              icon: 'none'
            });
            reject(data);
          }
        } else {
          wx.showToast({
            title: '网络请求失败',
            icon: 'none'
          });
          reject(res);
        }
      },
      fail(err) {
        wx.showToast({
          title: '网络连接失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

/**
 * GET请求
 */
const get = (url, data) => {
  return request({ url, method: 'GET', data });
};

/**
 * POST请求
 */
const post = (url, data) => {
  return request({ url, method: 'POST', data });
};

/**
 * PUT请求
 */
const put = (url, data) => {
  return request({ url, method: 'PUT', data });
};

/**
 * DELETE请求
 */
const del = (url, data) => {
  return request({ url, method: 'DELETE', data });
};

module.exports = {
  request,
  get,
  post,
  put,
  del
};
