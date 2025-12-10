// app.js
App({
  onLaunch() {
    // 每次启动小程序时清除桌号，需要重新扫码
    wx.removeStorageSync('tableNo');
    this.globalData.tableNo = '';
    
    // 检查登录状态
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
      this.globalData.isLoggedIn = true;
      // 获取用户信息
      this.getUserInfo();
    }
  },

  // 获取用户信息
  getUserInfo() {
    const that = this;
    wx.request({
      url: `${this.globalData.baseUrl}/getInfo`,
      method: 'GET',
      header: {
        'Authorization': 'Bearer ' + this.globalData.token
      },
      success(res) {
        if (res.data.code === 200) {
          that.globalData.userInfo = res.data.data;
        }
      }
    });
  },

  // 微信登录
  wxLogin(callback) {
    const that = this;
    wx.login({
      success(res) {
        if (res.code) {
          // 将code发送到后端换取openid
          wx.request({
            url: `${that.globalData.baseUrl}/wx/login`,
            method: 'POST',
            data: { code: res.code },
            success(loginRes) {
              if (loginRes.data.code === 200) {
                that.globalData.token = loginRes.data.data.token;
                that.globalData.isLoggedIn = true;
                wx.setStorageSync('token', loginRes.data.data.token);
                that.getUserInfo();
                callback && callback(true);
              } else {
                callback && callback(false);
              }
            },
            fail() {
              callback && callback(false);
            }
          });
        }
      }
    });
  },

  globalData: {
    baseUrl: 'http://192.168.8.224:8080', // 后端API地址（局域网IP）
    token: '',
    isLoggedIn: false,
    userInfo: null,
    tableNo: '', // 桌号
    cart: [], // 购物车
    cartTotal: 0 // 购物车总价
  }
});
