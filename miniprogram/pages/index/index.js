// pages/index/index.js - 扫码入口页

const app = getApp();

Page({
  data: {
    tableNo: ''
  },

  onLoad(options) {
    // 处理扫码进入的参数
    if (options.scene) {
      // 从场景值解码参数
      const scene = decodeURIComponent(options.scene);
      this.handleSceneParams(scene);
    } else if (options.tableNo) {
      this.setTableNo(options.tableNo);
    }
    
    // 检查本地存储的桌号
    const savedTableNo = wx.getStorageSync('tableNo');
    if (savedTableNo) {
      this.setData({ tableNo: savedTableNo });
      app.globalData.tableNo = savedTableNo;
    }
  },

  onShow() {
    // 隐藏tabBar，因为这是入口页
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: -1 });
    }
  },

  // 处理场景参数
  handleSceneParams(scene) {
    // 解析参数格式：tableNo=1
    const params = {};
    scene.split('&').forEach(item => {
      const [key, value] = item.split('=');
      params[key] = value;
    });

    if (params.tableNo) {
      this.setTableNo(params.tableNo);
    }
  },

  // 设置桌号
  setTableNo(tableNo) {
    this.setData({ tableNo });
    app.globalData.tableNo = tableNo;
    wx.setStorageSync('tableNo', tableNo);
  },

  // 扫描二维码
  scanQRCode() {
    wx.scanCode({
      scanType: ['qrCode'],
      success: (res) => {
        // 解析扫码结果
        const result = res.result;
        // 假设二维码内容格式为：tableNo=1 或 URL带参数
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
          this.setTableNo(tableNo);
          wx.showToast({
            title: `桌号${tableNo}`,
            icon: 'success'
          });
          // 检查登录状态后再跳转
          setTimeout(() => {
            const app = getApp();
            if (!app.globalData.isLoggedIn) {
              wx.showModal({
                title: '提示',
                content: '请先登录后再点餐',
                confirmText: '去登录',
                success: (modalRes) => {
                  if (modalRes.confirm) {
                    wx.navigateTo({
                      url: '/pages/login/login'
                    });
                  }
                }
              });
            } else {
              wx.switchTab({
                url: '/pages/menu/menu'
              });
            }
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

  // 跳转到点餐页
  goToMenu() {
    const app = getApp();
    // 检查是否登录
    if (!app.globalData.isLoggedIn) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再点餐',
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
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 跳转到订单页
  goToOrders() {
    const app = getApp();
    // 检查是否登录
    if (!app.globalData.isLoggedIn) {
      wx.showModal({
        title: '提示',
        content: '请先登录后查看订单',
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
    wx.switchTab({
      url: '/pages/order/order'
    });
  }
});
