<template>
	<view class="container">
		<view v-if="cart.length === 0" class="empty-cart">
			<text>购物车是空的</text>
			<button size="mini" @click="goMenu">去点餐</button>
		</view>
		<view v-else class="cart-list">
			<view class="cart-item" v-for="(item, index) in cart" :key="index">
				<image :src="item.pictureUrl" mode="aspectFill" class="item-img"></image>
				<view class="item-info">
					<text class="title">{{item.name}}</text>
					<text class="spec" v-if="item.spec">{{item.spec}}</text>
					<view class="price-box">
						<text class="price">¥{{item.price}}</text>
						<view class="number-box">
							<text class="btn" @click="updateCount(item, -1)">-</text>
							<text class="num">{{item.count}}</text>
							<text class="btn" @click="updateCount(item, 1)">+</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="footer" v-if="cart.length > 0">
			<view class="total">
				合计: <text class="price">¥{{cartTotal}}</text>
			</view>
			<button class="checkout-btn" @click="checkout">去结算</button>
		</view>
	</view>
</template>

<script>
	import { mapState, mapGetters, mapMutations } from 'vuex';
	
	export default {
		computed: {
			...mapState(['cart']),
			...mapGetters(['cartTotal'])
		},
		methods: {
			...mapMutations(['UPDATE_CART_COUNT']),
			goMenu() {
				uni.switchTab({
					url: '/pages/menu/menu'
				});
			},
			updateCount(item, change) {
				this.UPDATE_CART_COUNT({
					id: item.id,
					spec: item.spec,
					count: item.count + change
				});
			},
			checkout() {
				uni.navigateTo({
					url: '/pages/order/order'
				});
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding-bottom: 120rpx;
	}
	
	.empty-cart {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-top: 200rpx;
		
		text {
			margin-bottom: 30rpx;
			color: $uni-text-color-grey;
		}
	}
	
	.cart-list {
		padding: 20rpx;
	}
	
	.cart-item {
		display: flex;
		background-color: $uni-bg-color;
		padding: 20rpx;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		
		.item-img {
			width: 160rpx;
			height: 160rpx;
			border-radius: 8rpx;
			margin-right: 20rpx;
		}
		
		.item-info {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			
			.title {
				font-size: 30rpx;
				font-weight: bold;
			}
			
			.spec {
				font-size: 24rpx;
				color: $uni-text-color-grey;
			}
			
			.price-box {
				display: flex;
				justify-content: space-between;
				align-items: center;
				
				.price {
					color: $uni-color-error;
					font-size: 32rpx;
					font-weight: bold;
				}
				
				.number-box {
					display: flex;
					align-items: center;
					
					.btn {
						width: 50rpx;
						height: 50rpx;
						line-height: 50rpx;
						text-align: center;
						background-color: $uni-bg-color-grey;
						border-radius: 50%;
						font-size: 32rpx;
					}
					
					.num {
						margin: 0 20rpx;
						font-size: 28rpx;
					}
				}
			}
		}
	}
	
	.footer {
		position: fixed;
		bottom: var(--window-bottom);
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
		
		.checkout-btn {
			background-color: $uni-color-primary;
			color: #fff;
			border-radius: 40rpx;
			font-size: 30rpx;
			padding: 0 60rpx;
		}
	}
</style>
