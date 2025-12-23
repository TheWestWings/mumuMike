// pages/review/review.js - 评价/反馈页面

const app = getApp();
const { informationApi } = require('../../utils/api');

Page({
    data: {
        type: 'review', // review: 订单评价, feedback: 意见反馈
        orderId: null,
        rating: 5,
        ratingText: '非常满意',
        reviewTitle: '',
        reviewContent: '',
        submitting: false
    },

    onLoad(options) {
        if (options.type === 'feedback') {
            this.setData({ type: 'feedback' });
            wx.setNavigationBarTitle({ title: '意见反馈' });
        } else {
            wx.setNavigationBarTitle({ title: '订单评价' });
        }

        if (options.orderId) {
            this.setData({ orderId: options.orderId });
        }
    },

    // 设置评分
    setRating(e) {
        const value = e.currentTarget.dataset.value;
        const ratingTexts = ['很不满意', '不满意', '一般', '满意', '非常满意'];

        this.setData({
            rating: value,
            ratingText: ratingTexts[value - 1]
        });
    },

    // 标题输入
    onTitleInput(e) {
        this.setData({ reviewTitle: e.detail.value });
    },

    // 内容输入
    onContentInput(e) {
        this.setData({ reviewContent: e.detail.value });
    },

    // 提交评价/反馈
    async submitReview() {
        const { type, orderId, rating, reviewTitle, reviewContent } = this.data;

        if (!reviewTitle.trim()) {
            wx.showToast({
                title: '请输入标题',
                icon: 'none'
            });
            return;
        }

        if (!reviewContent.trim()) {
            wx.showToast({
                title: '请输入内容',
                icon: 'none'
            });
            return;
        }

        if (reviewContent.trim().length < 10) {
            wx.showToast({
                title: '内容至少10个字符',
                icon: 'none'
            });
            return;
        }

        this.setData({ submitting: true });

        try {
            // 构建提交数据
            const data = {
                title: reviewTitle,
                content: type === 'review'
                    ? `[评分:${rating}星] ${reviewContent}`
                    : reviewContent,
                userId: app.globalData.userInfo?.id
            };

            if (orderId) {
                data.content = `[订单:${orderId}] ${data.content}`;
            }

            const res = await informationApi.addInformation(data);

            if (res.code === 200) {
                // 标记订单已评价
                if (orderId) {
                    wx.request({
                        url: `${app.globalData.baseUrl}/order/updateOrder`,
                        method: 'PUT',
                        header: {
                            'Authorization': 'Bearer ' + app.globalData.token,
                            'Content-Type': 'application/json'
                        },
                        data: { id: parseInt(orderId), reviewed: 1 }
                    });
                }

                wx.showToast({
                    title: '评价成功',
                    icon: 'success'
                });

                setTimeout(() => {
                    wx.switchTab({
                        url: '/pages/order/order'
                    });
                }, 1500);
            }
        } catch (err) {
            console.error('提交失败', err);
            wx.showToast({
                title: '提交失败，请重试',
                icon: 'none'
            });
        } finally {
            this.setData({ submitting: false });
        }
    }
});
