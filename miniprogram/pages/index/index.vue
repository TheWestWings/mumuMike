<template>
	<view class="content">
		<image class="logo" src="/static/logo.png" mode="aspectFit"></image>
		<view class="text-area">
			<text class="title">欢迎光临 MumuMike</text>
		</view>
		
		<view class="btn-area">
			<button type="primary" class="scan-btn" @click="scanCode">
				<text class="iconfont icon-scan"></text> 扫码点餐
			</button>
		</view>
		
		<view class="manual-entry" @click="goToMenu">
			<text>直接进入菜单 (测试用)</text>
		</view>
	</view>
</template>

<script>
	import { mapMutations } from 'vuex';
	
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {

		},
		methods: {
			...mapMutations(['SET_TABLE_ID']),
			scanCode() {
				uni.scanCode({
					success: (res) => {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
						// Assuming result contains tableId, e.g., "table:123" or just "123"
						// Or a URL like "http://.../?tableId=123"
						let tableId = res.result;
						// Simple parsing logic
						if (tableId.includes('tableId=')) {
							tableId = tableId.split('tableId=')[1];
						}
						
						this.SET_TABLE_ID(tableId);
						
						uni.showToast({
							title: '扫码成功，桌号：' + tableId,
							icon: 'success'
						});
						
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/menu/menu'
							});
						}, 1500);
					},
					fail: (err) => {
						uni.showToast({
							title: '扫码失败',
							icon: 'none'
						});
					}
				});
			},
			goToMenu() {
				this.SET_TABLE_ID('Test-Table-1');
				uni.switchTab({
					url: '/pages/menu/menu'
				});
			}
		}
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
		background-color: #fff;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
		margin-bottom: 100rpx;
	}

	.title {
		font-size: 36rpx;
		color: #333;
		font-weight: bold;
	}
	
	.btn-area {
		width: 80%;
	}
	
	.scan-btn {
		background-color: $uni-color-primary;
		border-radius: 50rpx;
		height: 100rpx;
		line-height: 100rpx;
		font-size: 32rpx;
		box-shadow: 0 10rpx 20rpx rgba(0, 122, 255, 0.3);
	}
	
	.manual-entry {
		margin-top: 40rpx;
		color: $uni-text-color-grey;
		font-size: 24rpx;
		text-decoration: underline;
	}
</style>
