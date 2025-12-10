# 木木奶茶 微信小程序

## 项目简介

木木奶茶微信小程序是一个移动端点餐系统，与现有的电脑端管理系统和后台管理系统实现数据同步，为顾客提供便捷的点餐体验。

## 功能模块

### 核心功能

1. **二维码扫码进入**
   - 扫描餐桌二维码自动识别桌号
   - 进入点餐流程

2. **微信授权登录**
   - 支持微信一键登录
   - 支持账号密码登录
   - 支持新用户注册

3. **菜品浏览展示**
   - 分类导航
   - 商品列表
   - 商品详情
   - 价格展示

4. **购物车管理**
   - 添加/删除商品
   - 数量调整
   - 备注功能
   - 实时价格计算

5. **在线下单**
   - 订单提交
   - 订单确认
   - 支付功能（预留微信支付接口）

6. **订单状态跟踪**
   - 进行中订单
   - 已完成订单
   - 订单详情
   - 消息通知

7. **评价反馈**
   - 订单评价
   - 意见反馈

## 项目结构

```
miniprogram/
├── app.js                # 小程序入口文件
├── app.json              # 小程序配置文件
├── app.wxss              # 全局样式
├── project.config.json   # 项目配置
├── sitemap.json          # 站点地图配置
├── pages/                # 页面目录
│   ├── index/            # 首页（扫码入口）
│   ├── menu/             # 点餐页
│   ├── cart/             # 购物车页
│   ├── order/            # 订单列表页
│   ├── orderDetail/      # 订单详情页
│   ├── user/             # 个人中心页
│   ├── login/            # 登录页
│   └── review/           # 评价/反馈页
├── utils/                # 工具函数
│   ├── request.js        # HTTP请求封装
│   ├── api.js            # API接口封装
│   ├── util.js           # 通用工具函数
│   └── cart.js           # 购物车管理
├── components/           # 公共组件
└── images/               # 图片资源
```

## 技术栈

- 微信小程序原生开发
- WXSS 响应式样式
- 微信支付SDK（预留）

## API接口

与后端对接的主要接口：

### 用户相关
- `POST /login` - 账号登录
- `POST /wx/login` - 微信登录
- `POST /register` - 用户注册
- `GET /getInfo` - 获取用户信息

### 商品相关
- `GET /product/getProductVOList` - 获取商品分类列表
- `GET /product/{id}` - 获取商品详情

### 订单相关
- `POST /order` - 创建订单
- `POST /order/getListVO` - 获取订单列表
- `GET /order/getOrderVOById/{id}` - 获取订单详情

### 消息相关
- `GET /message/getMessageListByUserId/{userId}` - 获取消息列表
- `PUT /message` - 更新消息状态

### 反馈相关
- `POST /information` - 提交反馈

## 使用说明

### 开发环境配置

1. 下载并安装 [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)

2. 打开微信开发者工具，选择"导入项目"

3. 项目目录选择 `miniprogram` 文件夹

4. 填入 AppID（测试可使用测试号）

5. 点击"导入"即可开始开发

### 后端配置

1. 在 `app.js` 中修改 `baseUrl` 为实际的后端API地址：
```javascript
globalData: {
  baseUrl: 'http://your-server-ip:8080',
  // ...
}
```

2. 如需使用微信登录功能，在后端 `application.properties` 中配置：
```properties
wx.appid=你的小程序appid
wx.secret=你的小程序secret
```

### 生成餐桌二维码

二维码内容格式：
- 方式1: `tableNo=1`（推荐）
- 方式2: 直接使用桌号数字，如 `1`

## 设计规范

### 主题色
- 主色调: #8d6e63
- 辅助色: #a88e85
- 深色: #5d4037
- 背景色: #FDF6E3

### 状态色
- 成功: #67c23a
- 警告: #e6a23c
- 危险: #f56c6c
- 信息: #909399

## 注意事项

1. 小程序需要在微信公众平台注册并获取 AppID
2. 真机调试需要配置服务器域名白名单
3. 微信支付功能需要开通微信支付商户号
4. 生产环境需要使用 HTTPS 协议

## 版本历史

- v1.0.0 (2024-12-04)
  - 初始版本发布
  - 完成基础点餐功能
  - 完成订单管理功能
  - 完成用户中心功能
