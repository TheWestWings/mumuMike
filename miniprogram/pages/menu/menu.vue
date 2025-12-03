<template>
	<view class="container">
		<view class="main">
			<scroll-view scroll-y class="left-aside">
				<view v-for="item in categories" :key="item.id" class="f-item b-b" :class="{active: item.id === currentId}" @click="tabtap(item)">
					{{item.title}}
				</view>
			</scroll-view>
			<scroll-view scroll-with-animation scroll-y class="right-aside" @scroll="asideScroll" :scroll-top="tabScrollTop">
				<view v-for="item in categories" :key="item.id" class="s-list" :id="'main-'+item.id">
					<text class="s-item">{{item.title}}</text>
					<view class="t-list">
						<view class="t-item" v-for="product in item.productList" :key="product.id">
							<image :src="product.pictureUrl" mode="aspectFill" class="t-img" @click="navToDetail(product)"></image>
							<view class="right-content">
								<view class="title">{{product.name}}</view>
								<view class="desc">{{product.description}}</view>
								<view class="price-box">
									<text class="price">¥{{product.price}}</text>
									<view class="action-box">
										<view class="minus-btn" v-if="getCartCount(product.id) > 0" @click.stop="decreaseCart(product)">-</view>
										<text class="count" v-if="getCartCount(product.id) > 0">{{getCartCount(product.id)}}</text>
										<view class="add-btn" @click.stop="addToCart(product)">+</view>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		
		<!-- Bottom Cart Bar -->
		<view class="cart-bar" v-if="cartCount > 0" @click="goToCart">
			<view class="cart-icon">
				<text class="iconfont icon-cart"></text>
				<text class="badge">{{cartCount}}</text>
			</view>
			<view class="price-info">
				<text class="total-price">¥{{cartTotal}}</text>
			</view>
			<view class="pay-btn">去结算</view>
		</view>
	</view>
</template>

<script>
	import { getProductVOList } from '@/api/index.js';
	import { mapGetters, mapMutations, mapState } from 'vuex';
	
	export default {
		data() {
			return {
				categories: [],
				currentId: 0,
				tabScrollTop: 0
			}
		},
		computed: {
			...mapGetters(['cartCount', 'cartTotal']),
			...mapState(['cart'])
		},
		onLoad() {
			this.loadData();
		},
		methods: {
			...mapMutations(['ADD_TO_CART', 'UPDATE_CART_COUNT']),
			async loadData() {
				try {
					const res = await getProductVOList();
					// res.rows contains the list
					if (res.rows) {
						this.categories = res.rows;
						if (this.categories.length > 0) {
							this.currentId = this.categories[0].id;
						}
					}
				} catch (e) {
					console.error(e);
				}
			},
			getCartCount(id) {
				const item = this.cart.find(p => p.id === id && p.spec === '');
				return item ? item.count : 0;
			},
			decreaseCart(product) {
				const count = this.getCartCount(product.id);
				if (count > 0) {
					this.UPDATE_CART_COUNT({
						id: product.id,
						spec: '',
						count: count - 1
					});
				}
			},
			tabtap(item) {
				this.currentId = item.id;
				// Scroll to right section
				// Simple implementation: just set scrollTop based on index or calculation
				// For now, just let it be or implement proper scroll linking
			},
			asideScroll(e) {
				// Implement scroll spy if needed
			},
			navToDetail(product) {
				// Optional: go to product detail
			},
			addToCart(product) {
				this.ADD_TO_CART({
					...product,
					count: 1,
					spec: '' // Default spec
				});
				uni.showToast({
					title: '已加入购物车',
					icon: 'none'
				});
			},
			goToCart() {
				uni.switchTab({
					url: '/pages/cart/cart'
				});
			}
		}
	}
</script>

<style lang="scss">
	.container {
		height: 100vh;
		display: flex;
		flex-direction: column;
		background-color: #FDF6E3; /* Web version background */
	}
	
	.main {
		flex: 1;
		display: flex;
		overflow: hidden;
	}
	
	.left-aside {
		flex: 0 0 200rpx;
		background-color: #fff; /* Changed to white or keep grey? Web uses #FDF6E3 for main, maybe sidebar white? */
		/* Let's keep sidebar distinct but maybe lighter */
		background-color: #f2f2f2;
		height: 100%;
	}
	
	.f-item {
		height: 100rpx;
		line-height: 100rpx;
		text-align: center;
		font-size: 28rpx;
		color: #666;
		position: relative;
		
		&.active {
			background-color: #FDF6E3; /* Match right side bg */
			color: #333;
			font-weight: bold;
			
			&:before {
				content: '';
				position: absolute;
				left: 0;
				top: 30rpx;
				height: 40rpx;
				width: 8rpx;
				background-color: #333; /* Darker accent */
				border-radius: 0 4rpx 4rpx 0;
			}
		}
	}
	
	.right-aside {
		flex: 1;
		background-color: #FDF6E3; /* Web version background */
		height: 100%;
		padding-bottom: 100rpx; // Space for cart bar
	}
	
	.s-list {
		width: 100%;
	}
	
	.s-item {
		display: block;
		height: 60rpx;
		line-height: 60rpx;
		font-size: 26rpx;
		font-weight: bold;
		color: #666;
		background-color: transparent; /* Remove grey bg */
		padding-left: 30rpx;
		margin-top: 10rpx;
	}
	
	.t-list {
		padding: 20rpx;
	}
	
	.t-item {
		display: flex;
		margin-bottom: 20rpx;
		background-color: #fff;
		padding: 20rpx;
		border-radius: 12rpx; /* Web version radius */
		box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.05); /* Web version shadow */
		
		.t-img {
			width: 160rpx; /* Slightly larger */
			height: 160rpx;
			border-radius: 8rpx;
			margin-right: 20rpx;
			flex-shrink: 0; /* Prevent image shrinking */
		}
		
		.right-content {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			min-width: 0; /* Fix for flex item overflow */
			height: 160rpx; /* Match image height to ensure vertical spacing */
			
			.title {
				font-size: 30rpx;
				color: #333;
				font-weight: bold;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
			
			.desc {
				font-size: 24rpx;
				color: #999;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				margin: 10rpx 0;
			}
			
			.price-box {
				display: flex;
				justify-content: space-between;
				align-items: center;
				
				.price {
					font-size: 32rpx;
					color: #dd524d;
					font-weight: bold;
				}
				
				.action-box {
					display: flex;
					align-items: center;
					flex-shrink: 0; /* Prevent compression */
					
					.add-btn, .minus-btn {
						width: 50rpx;
						height: 50rpx;
						line-height: 50rpx;
						text-align: center;
						border-radius: 50%;
						font-size: 32rpx;
						font-weight: bold;
					}
					
					.add-btn {
						background-color: $uni-color-primary;
						color: #fff;
					}
					
					.minus-btn {
						background-color: #fff;
						border: 1px solid #ccc;
						color: #666;
						box-sizing: border-box;
						line-height: 46rpx; /* Adjust for border */
					}
					
					.count {
						margin: 0 20rpx;
						font-size: 28rpx;
						color: #333;
					}
				}
			}
		}
	}
	
	.cart-bar {
		position: fixed;
		bottom: var(--window-bottom);
		left: 30rpx;
		right: 30rpx;
		height: 100rpx;
		background-color: rgba(0,0,0,0.9);
		display: flex;
		align-items: center;
		padding: 0 30rpx;
		z-index: 99;
		border-radius: 50rpx; /* Floating pill style */
		box-shadow: 0 10rpx 20rpx rgba(0,0,0,0.2);
		margin-bottom: 20rpx;
		
		.cart-icon {
			position: relative;
			margin-right: 30rpx;
			color: #fff;
			
			.badge {
				position: absolute;
				top: -10rpx;
				right: -10rpx;
				background-color: #dd524d;
				color: #fff;
				font-size: 20rpx;
				padding: 0 10rpx;
				border-radius: 20rpx;
			}
		}
		
		.price-info {
			flex: 1;
			color: #fff;
			font-size: 32rpx;
			font-weight: bold;
		}
		
		.pay-btn {
			background-color: #f0ad4e; /* Warning color or primary */
			color: #fff;
			padding: 0 40rpx;
			height: 70rpx;
			line-height: 70rpx;
			border-radius: 35rpx;
			font-size: 28rpx;
		}
	}
</style>
