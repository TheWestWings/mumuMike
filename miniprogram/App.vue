<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
			// Check login status
			const token = uni.getStorageSync('token');
			if (!token) {
				// uni.reLaunch({
				// 	url: '/pages/login/login'
				// });
			}
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/* 每个页面公共css */
	@import '@/uni.scss';
	
	page {
		background-color: #f5f5f5;
		font-size: 28rpx;
		color: #333;
	}
</style>
