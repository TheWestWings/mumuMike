<template>
	<view class="container">
		<view class="form-group">
			<text class="label">标题</text>
			<input class="input" v-model="title" placeholder="请输入标题" />
		</view>
		<view class="form-group">
			<text class="label">内容</text>
			<textarea class="textarea" v-model="content" placeholder="请输入您的宝贵意见" />
		</view>
		<button class="submit-btn" @click="submit" :loading="loading">提交反馈</button>
	</view>
</template>

<script>
	import { createFeedback } from '@/api/index.js';
	import { mapState } from 'vuex';
	
	export default {
		data() {
			return {
				title: '',
				content: '',
				loading: false
			}
		},
		computed: {
			...mapState(['userInfo'])
		},
		methods: {
			async submit() {
				if (!this.title || !this.content) {
					uni.showToast({ title: '请填写完整', icon: 'none' });
					return;
				}
				
				this.loading = true;
				try {
					const res = await createFeedback({
						userId: this.userInfo.id || 1,
						title: this.title,
						content: this.content,
						status: 0,
						createTime: new Date()
					});
					
					if (res === 1 || res.code === 200) { // Controller returns int or AjaxResult?
						// InformationController returns int.
						uni.showToast({ title: '提交成功' });
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					} else {
						uni.showToast({ title: '提交失败', icon: 'none' });
					}
				} catch (e) {
					console.error(e);
					uni.showToast({ title: '提交失败', icon: 'none' });
				} finally {
					this.loading = false;
				}
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding: 30rpx;
	}
	
	.form-group {
		margin-bottom: 30rpx;
		
		.label {
			display: block;
			margin-bottom: 10rpx;
			font-weight: bold;
		}
		
		.input {
			border: 1px solid #eee;
			height: 80rpx;
			padding: 0 20rpx;
			border-radius: 8rpx;
			background-color: #fff;
		}
		
		.textarea {
			border: 1px solid #eee;
			width: 100%;
			height: 200rpx;
			padding: 20rpx;
			border-radius: 8rpx;
			background-color: #fff;
			box-sizing: border-box;
		}
	}
	
	.submit-btn {
		background-color: $uni-color-primary;
		color: #fff;
		margin-top: 50rpx;
	}
</style>
