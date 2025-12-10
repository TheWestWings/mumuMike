// pages/user/user.js - 个人中心页

const app = getApp();
const { messageApi, userApi } = require('../../utils/api');

Page({
  data: {
    isLoggedIn: false,
    userInfo: null,
    unreadCount: 0,
    messageList: [],
    showMessages: false
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    this.checkLoginStatus();
    if (app.globalData.isLoggedIn) {
      this.loadMessages();
    }
    
    // 设置tabbar选中状态
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 2 });
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    const isLoggedIn = app.globalData.isLoggedIn;
    const userInfo = app.globalData.userInfo;
    
    this.setData({ 
      isLoggedIn, 
      userInfo 
    });
  },

  // 加载消息
  async loadMessages() {
    if (!app.globalData.userInfo?.id) return;

    try {
      const res = await messageApi.getMessageList(app.globalData.userInfo.id);
      if (res.code === 200) {
        const messageList = res.rows || [];
        const unreadCount = messageList.filter(m => m.status === 0).length;
        
        this.setData({ messageList, unreadCount });
      }
    } catch (err) {
      console.error('加载消息失败', err);
    }
  },

  // 跳转登录
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 跳转订单
  goToOrders() {
    wx.switchTab({
      url: '/pages/order/order'
    });
  },

  // 显示消息
  goToMessages() {
    if (!this.data.isLoggedIn) {
      this.goToLogin();
      return;
    }
    this.setData({ showMessages: true });
  },

  // 关闭消息
  closeMessages() {
    this.setData({ showMessages: false });
  },

  // 阅读消息
  async readMessage(e) {
    const index = e.currentTarget.dataset.index;
    const message = this.data.messageList[index];
    
    if (message.status === 0) {
      try {
        await messageApi.updateMessage({ id: message.id, status: 1 });
        
        const messageList = this.data.messageList;
        messageList[index].status = 1;
        const unreadCount = messageList.filter(m => m.status === 0).length;
        
        this.setData({ messageList, unreadCount });
      } catch (err) {
        console.error('更新消息状态失败', err);
      }
    }
  },

  // 意见反馈
  goToFeedback() {
    if (!this.data.isLoggedIn) {
      this.goToLogin();
      return;
    }
    wx.navigateTo({
      url: '/pages/review/review?type=feedback'
    });
  },

  // 扫码点餐
  scanQRCode() {
    wx.scanCode({
      scanType: ['qrCode'],
      success: (res) => {
        const result = res.result;
        let tableNo = '';
        
        if (result.includes('tableNo=')) {
          const match = result.match(/tableNo=(\d+)/);
          if (match) {
            tableNo = match[1];
          }
        } else if (/^\d+$/.test(result)) {
          tableNo = result;
        }

        if (tableNo) {
          app.globalData.tableNo = tableNo;
          wx.setStorageSync('tableNo', tableNo);
          wx.showToast({
            title: `桌号${tableNo}`,
            icon: 'success'
          });
          setTimeout(() => {
            wx.switchTab({
              url: '/pages/menu/menu'
            });
          }, 1000);
        } else {
          wx.showToast({
            title: '无效的二维码',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        if (err.errMsg.indexOf('cancel') === -1) {
          wx.showToast({
            title: '扫码失败',
            icon: 'none'
          });
        }
      }
    });
  },

  // 关于我们
  showAbout() {
    wx.showModal({
      title: '关于木木奶茶',
      content: '木木奶茶 - 用心制作的奶香甜品\n\n我们致力于为您提供优质的奶茶体验，每一杯都是用心之作。',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 联系客服
  contactService() {
    wx.showModal({
      title: '联系客服',
      content: '客服电话：400-888-8888\n服务时间：9:00-22:00',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 退出登录
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除登录状态
          app.globalData.token = '';
          app.globalData.isLoggedIn = false;
          app.globalData.userInfo = null;
          wx.removeStorageSync('token');
          
          this.setData({
            isLoggedIn: false,
            userInfo: null,
            unreadCount: 0,
            messageList: []
          });

          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
      }
    });
  }
});
