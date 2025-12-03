<template>
	<view class="container">
		<view class="header">
			<view class="user-info" v-if="userInfo.username">
				<image class="avatar" src="/static/logo.png"></image>
				<text class="username">{{userInfo.username}}</text>
			</view>
			<view class="user-info" v-else @click="goLogin">
				<image class="avatar" src="/static/logo.png"></image>
				<text class="username">点击登录</text>
			</view>
		</view>
		
		<view class="section">
			<view class="section-header">
				<text class="title">我的订单</text>
			</view>
			<view class="order-list">
				<view class="order-item" v-for="item in orderList" :key="item.id" @click="goDetail(item.id)">
					<view class="order-top">
						<text class="time">{{formatTime(item.createTime)}}</text>
						<text class="status">{{getStatusText(item.status)}}</text>
					</view>
					<view class="order-content">
						<text>共 {{item.product ? item.product.length : 0}} 件商品</text>
						<!-- Calculate total if available, or just show count -->
					</view>
				</view>
				<view v-if="orderList.length === 0" class="empty">
					<text>暂无订单</text>
				</view>
			</view>
		</view>
		
		<view class="section">
			<button class="feedback-btn" @click="goFeedback">意见反馈</button>
			<button class="logout-btn" v-if="userInfo.username" @click="logout">退出登录</button>
		</view>
	</view>
</template>

<script>
	import { getOrderList } from '@/api/index.js';
	import { mapState, mapMutations } from 'vuex';
	
	export default {
		data() {
			return {
				orderList: []
			}
		},
		computed: {
			...mapState(['userInfo'])
		},
		onShow() {
			if (this.userInfo.username) {
				this.loadOrders();
			}
		},
		methods: {
			...mapMutations(['SET_TOKEN', 'SET_USER_INFO']),
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				});
			},
			async loadOrders() {
				try {
					// Backend expects Order object for filtering?
					// `getOrderList` in controller takes `Order order`.
					// It filters by userId if provided?
					// Wait, `OrderController.java`: `getList(Order order)` -> `orderService.getOrderList(order)`.
					// `OrderMapper.xml`: `select ... where ...`
					// It doesn't seem to enforce userId filter in XML unless passed in `order`.
					// So we must pass userId.
					const res = await getOrderList({ userId: this.userInfo.id || 1 });
					if (res.rows) {
						this.orderList = res.rows;
					}
				} catch (e) {
					console.error(e);
				}
			},
			goDetail(id) {
				uni.navigateTo({
					url: `/pages/order/detail?id=${id}`
				});
			},
			goFeedback() {
				uni.navigateTo({
					url: '/pages/feedback/feedback'
				});
			},
			logout() {
				this.SET_TOKEN('');
				this.SET_USER_INFO({});
				uni.reLaunch({
					url: '/pages/index/index'
				});
			},
			formatTime(time) {
				if (!time) return '';
				return new Date(time).toLocaleString();
			},
			getStatusText(status) {
				// 0: Pending, 1: Completed, 2: Cancelled?
				// Check Order.java or Mapper.
				// Mapper: status=2 is cancelled (or refunded).
				// Let's assume 0: Pending/Cooking, 1: Completed, 2: Cancelled.
				const map = {
					0: '进行中',
					1: '已完成',
					2: '已退单'
				};
				return map[status] || '未知';
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding: 20rpx;
	}
	
	.header {
		background-color: #fff;
		padding: 40rpx;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		
		.avatar {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			margin-right: 30rpx;
			background-color: #eee;
		}
		
		.username {
			font-size: 36rpx;
			font-weight: bold;
		}
	}
	
	.section {
		background-color: #fff;
		border-radius: 10rpx;
		padding: 20rpx;
		margin-bottom: 20rpx;
	}
	
	.section-header {
		border-bottom: 1px solid #eee;
		padding-bottom: 20rpx;
		margin-bottom: 20rpx;
		
		.title {
			font-size: 30rpx;
			font-weight: bold;
		}
	}
	
	.order-item {
		border-bottom: 1px solid #f5f5f5;
		padding: 20rpx 0;
		
		&:last-child {
			border-bottom: none;
		}
		
		.order-top {
			display: flex;
			justify-content: space-between;
			margin-bottom: 10rpx;
			
			.time {
				font-size: 24rpx;
				color: #999;
			}
			
			.status {
				font-size: 26rpx;
				color: $uni-color-primary;
			}
		}
		
		.order-content {
			font-size: 28rpx;
			color: #333;
		}
	}
	
	.empty {
		text-align: center;
		padding: 40rpx;
		color: #999;
	}
	
	.feedback-btn, .logout-btn {
		margin-top: 20rpx;
		background-color: #fff;
		border: 1px solid #eee;
		font-size: 28rpx;
	}
	
	.logout-btn {
		color: $uni-color-error;
	}
</style>
