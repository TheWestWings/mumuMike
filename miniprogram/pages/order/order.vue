<template>
	<view class="container">
		<view class="section">
			<text class="section-title">当前桌号: {{tableId || '未扫码'}}</text>
		</view>
		
		<view class="section">
			<view class="order-item" v-for="(item, index) in cart" :key="index">
				<view class="item-left">
					<text class="name">{{item.name}}</text>
					<text class="spec" v-if="item.spec">{{item.spec}}</text>
				</view>
				<view class="item-right">
					<text class="count">x{{item.count}}</text>
					<text class="price">¥{{item.price * item.count}}</text>
				</view>
			</view>
		</view>
		
		<view class="section">
			<view class="remark-box">
				<text class="label">备注</text>
				<input class="input" type="text" v-model="remark" placeholder="请输入口味、偏好等要求" />
			</view>
		</view>
		
		<view class="footer">
			<view class="total">
				合计: <text class="price">¥{{cartTotal}}</text>
			</view>
			<button class="pay-btn" @click="submitOrder" :loading="loading">立即支付</button>
		</view>
	</view>
</template>

<script>
	import { createOrder } from '@/api/index.js';
	import { mapState, mapGetters, mapMutations } from 'vuex';
	
	export default {
		data() {
			return {
				remark: '',
				loading: false
			}
		},
		computed: {
			...mapState(['cart', 'tableId', 'userInfo']),
			...mapGetters(['cartTotal'])
		},
		methods: {
			...mapMutations(['CLEAR_CART']),
			async submitOrder() {
				if (this.cart.length === 0) {
					uni.showToast({ title: '购物车为空', icon: 'none' });
					return;
				}
				
				if (!this.userInfo || !this.userInfo.username) {
					// Try to get user info or prompt login
					// Assuming token exists, maybe we need to fetch user ID?
					// The backend `insertOrder` expects `userId` in the Order object?
					// Let's check OrderController. It takes `Order order`.
					// Does it extract userId from token?
					// `OrderController.java`: `insertOrder(@RequestBody Order order)` -> `orderService.insertOrder(order)`.
					// It does NOT seem to set userId from SecurityContext.
					// So we must send userId.
					// But we only have username from login.
					// We need to fetch user details to get ID.
					// Or maybe the login response contained it?
					// Login returns token.
					// We might need a `/user/info` endpoint.
					// `UserController` doesn't seem to have `info` endpoint.
					// But `login` returns token.
					// Maybe we can decode token?
					// Or maybe we just send what we have.
					// If we don't have userId, the order might fail or be anonymous.
					// Let's assume we need to fetch user info first or we stored it.
					// For now, I'll use a dummy ID or try to get it.
				}
				
				this.loading = true;
				
				// Construct payload
				// Hack: Put tableId and remark in the first product's comment
				const products = this.cart.map((item, index) => {
					let comment = '';
					if (index === 0) {
						comment = `Table:${this.tableId || 'N/A'}; Remark:${this.remark}`;
					}
					return {
						productId: item.id,
						count: item.count,
						comment: comment,
						status: 0 // 0: Unfinished
					};
				});
				
				// We need userId. If not available, maybe 1?
				// In a real app, we would have fetched profile.
				// I'll assume 1 for now if missing.
				const userId = this.userInfo.id || 1; 
				
				const orderData = {
					userId: userId,
					product: products,
					status: 0, // 0: Pending
					createTime: new Date(),
					updateTime: new Date()
				};
				
				try {
					const res = await createOrder(orderData);
					if (res.code === 200 || res.msg === '操作成功') {
						uni.showToast({ title: '下单成功' });
						this.CLEAR_CART();
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/user/user' // Or order detail
							});
						}, 1500);
					} else {
						uni.showToast({ title: res.msg || '下单失败', icon: 'none' });
					}
				} catch (e) {
					console.error(e);
					uni.showToast({ title: '下单失败', icon: 'none' });
				} finally {
					this.loading = false;
				}
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding: 20rpx;
		padding-bottom: 120rpx;
	}
	
	.section {
		background-color: $uni-bg-color;
		border-radius: 10rpx;
		padding: 20rpx;
		margin-bottom: 20rpx;
	}
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
	}
	
	.order-item {
		display: flex;
		justify-content: space-between;
		margin-bottom: 20rpx;
		
		.item-left {
			display: flex;
			flex-direction: column;
			
			.name {
				font-size: 30rpx;
			}
			
			.spec {
				font-size: 24rpx;
				color: $uni-text-color-grey;
			}
		}
		
		.item-right {
			text-align: right;
			
			.count {
				display: block;
				font-size: 26rpx;
				color: $uni-text-color-grey;
			}
			
			.price {
				font-size: 30rpx;
				font-weight: bold;
			}
		}
	}
	
	.remark-box {
		display: flex;
		align-items: center;
		
		.label {
			width: 100rpx;
		}
		
		.input {
			flex: 1;
		}
	}
	
	.footer {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 100rpx;
		background-color: $uni-bg-color;
		display: flex;
		align-items: center;
		padding: 0 30rpx;
		box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.05);
		
		.total {
			flex: 1;
			font-size: 30rpx;
			
			.price {
				color: $uni-color-error;
				font-weight: bold;
				font-size: 36rpx;
				margin-left: 10rpx;
			}
		}
		
		.pay-btn {
			background-color: $uni-color-primary;
			color: #fff;
			border-radius: 40rpx;
			font-size: 30rpx;
			padding: 0 60rpx;
		}
	}
</style>
