// pages/login/login.js - 登录页面

const app = getApp();
const { userApi } = require('../../utils/api');

Page({
  data: {
    loginType: 'account', // account
    username: '',
    password: '',
    loading: false,
    wxLoading: false,
    agreed: false,
    // 注册相关
    showRegister: false,
    regUsername: '',
    regPassword: '',
    regEmail: '',
    regPhone: '',
    regLoading: false
  },

  // 用户名输入
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  // 密码输入
  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  // 切换协议同意
  toggleAgreement() {
    this.setData({ agreed: !this.data.agreed });
  },

  // 账号登录
  async accountLogin() {
    const { username, password, agreed } = this.data;

    if (!agreed) {
      wx.showToast({
        title: '请先同意用户协议',
        icon: 'none'
      });
      return;
    }

    if (!username.trim()) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await userApi.login({ username, password });
      
      if (res.code === 200) {
        // 保存登录信息
        app.globalData.token = res.data;
        app.globalData.isLoggedIn = true;
        wx.setStorageSync('token', res.data);

        // 获取用户信息
        app.getUserInfo();

        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }
    } catch (err) {
      console.error('登录失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  // 微信登录
  wxLogin() {
    const { agreed } = this.data;

    if (!agreed) {
      wx.showToast({
        title: '请先同意用户协议',
        icon: 'none'
      });
      return;
    }

    this.setData({ wxLoading: true });

    // 调用微信登录
    app.wxLogin((success) => {
      this.setData({ wxLoading: false });
      
      if (success) {
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: '登录失败，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 显示注册弹窗
  goToRegister() {
    this.setData({ showRegister: true });
  },

  // 关闭注册弹窗
  closeRegister() {
    this.setData({
      showRegister: false,
      regUsername: '',
      regPassword: '',
      regEmail: '',
      regPhone: ''
    });
  },

  // 注册相关输入
  onRegUsernameInput(e) {
    this.setData({ regUsername: e.detail.value });
  },

  onRegPasswordInput(e) {
    this.setData({ regPassword: e.detail.value });
  },

  onRegEmailInput(e) {
    this.setData({ regEmail: e.detail.value });
  },

  onRegPhoneInput(e) {
    this.setData({ regPhone: e.detail.value });
  },

  // 注册
  async register() {
    const { regUsername, regPassword, regEmail, regPhone } = this.data;

    if (!regUsername.trim()) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!regPassword || regPassword.length < 6) {
      wx.showToast({
        title: '密码至少6位',
        icon: 'none'
      });
      return;
    }

    this.setData({ regLoading: true });

    try {
      const res = await userApi.register({
        username: regUsername,
        password: regPassword,
        email: regEmail,
        phone: regPhone
      });

      if (res.code === 200) {
        wx.showToast({
          title: '注册成功',
          icon: 'success'
        });

        // 自动填充登录信息
        this.setData({
          username: regUsername,
          password: regPassword
        });

        this.closeRegister();
      }
    } catch (err) {
      console.error('注册失败', err);
    } finally {
      this.setData({ regLoading: false });
    }
  }
});
