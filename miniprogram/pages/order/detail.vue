<template>
	<view class="container">
		<view class="status-box">
			<text class="status-text">{{getStatusText(order.status)}}</text>
			<text class="time">{{formatTime(order.createTime)}}</text>
		</view>
		
		<view class="product-list">
			<view class="product-item" v-for="item in order.product" :key="item.id">
				<view class="info">
					<text class="name">{{item.product.name}}</text>
					<text class="count">x{{item.count}}</text>
				</view>
				<view class="price">
					<text>¥{{item.product.price}}</text>
				</view>
			</view>
		</view>
		
		<view class="info-box">
			<text>订单编号: {{order.id}}</text>
		</view>
	</view>
</template>

<script>
	import { getOrderById } from '@/api/index.js';
	
	export default {
		data() {
			return {
				order: {}
			}
		},
		onLoad(options) {
			if (options.id) {
				this.loadOrder(options.id);
			}
		},
		methods: {
			async loadOrder(id) {
				try {
					const res = await getOrderById(id);
					if (res.data) {
						this.order = res.data;
					}
				} catch (e) {
					console.error(e);
				}
			},
			formatTime(time) {
				if (!time) return '';
				return new Date(time).toLocaleString();
			},
			getStatusText(status) {
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
	
	.status-box {
		background-color: $uni-color-primary;
		color: #fff;
		padding: 40rpx;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.status-text {
			font-size: 40rpx;
			font-weight: bold;
			margin-bottom: 10rpx;
		}
		
		.time {
			font-size: 24rpx;
			opacity: 0.8;
		}
	}
	
	.product-list {
		background-color: #fff;
		border-radius: 10rpx;
		padding: 20rpx;
		margin-bottom: 20rpx;
	}
	
	.product-item {
		display: flex;
		justify-content: space-between;
		padding: 20rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		.info {
			display: flex;
			flex-direction: column;
			
			.name {
				font-size: 30rpx;
				font-weight: bold;
			}
			
			.count {
				font-size: 26rpx;
				color: #999;
			}
		}
	}
	
	.info-box {
		background-color: #fff;
		border-radius: 10rpx;
		padding: 20rpx;
		color: #666;
		font-size: 26rpx;
	}
</style>
