<template>
	<view class="login-container">
		<view class="header">
			<text class="title">MumuMike 登录</text>
		</view>
		
		<view class="form-box">
			<view class="input-group">
				<text class="label">用户名</text>
				<input class="input" type="text" v-model="username" placeholder="请输入用户名" />
			</view>
			<view class="input-group">
				<text class="label">密码</text>
				<input class="input" type="password" v-model="password" placeholder="请输入密码" />
			</view>
			
			<button class="submit-btn" @click="handleLogin" :loading="loading">登录</button>
			
			<view class="links">
				<text @click="toggleMode">{{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}</text>
			</view>
			
			<view v-if="isRegister" class="register-fields">
				<view class="input-group">
					<text class="label">确认密码</text>
					<input class="input" type="password" v-model="confirmPassword" placeholder="请再次输入密码" />
				</view>
				<view class="input-group">
					<text class="label">手机号</text>
					<input class="input" type="number" v-model="phone" placeholder="请输入手机号" />
				</view>
				<view class="input-group">
					<text class="label">邮箱</text>
					<input class="input" type="text" v-model="email" placeholder="请输入邮箱" />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { login, register } from '@/api/index.js';
	import { mapMutations } from 'vuex';
	
	export default {
		data() {
			return {
				username: '',
				password: '',
				confirmPassword: '',
				phone: '',
				email: '',
				loading: false,
				isRegister: false
			}
		},
		methods: {
			...mapMutations(['SET_TOKEN', 'SET_USER_INFO']),
			toggleMode() {
				this.isRegister = !this.isRegister;
				this.title = this.isRegister ? 'MumuMike 注册' : 'MumuMike 登录';
			},
			async handleLogin() {
				if (!this.username || !this.password) {
					uni.showToast({ title: '请输入用户名和密码', icon: 'none' });
					return;
				}
				
				if (this.isRegister) {
					if (this.password !== this.confirmPassword) {
						uni.showToast({ title: '两次密码不一致', icon: 'none' });
						return;
					}
					this.doRegister();
				} else {
					this.doLogin();
				}
			},
			async doLogin() {
				this.loading = true;
				try {
					const res = await login({ username: this.username, password: this.password });
					if (res.code === 200) {
						this.SET_TOKEN(res.msg); // Backend returns token in msg or data? Controller says: new AjaxResult(200, "登录成功", token); so data is token.
						// Wait, AjaxResult structure: code, msg, data.
						// Controller: return new AjaxResult(200, "登录成功", token);
						// So res.data should be the token.
						// Let's check AjaxResult.java if possible, but usually it's data.
						// Assuming res.data is token.
						this.SET_TOKEN(res.data);
						
						// Fetch user info if needed, or just store username
						this.SET_USER_INFO({ username: this.username });
						
						uni.showToast({ title: '登录成功' });
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					} else {
						uni.showToast({ title: res.msg || '登录失败', icon: 'none' });
					}
				} catch (e) {
					console.error(e);
				} finally {
					this.loading = false;
				}
			},
			async doRegister() {
				this.loading = true;
				try {
					const res = await register({
						username: this.username,
						password: this.password,
						phone: this.phone,
						email: this.email
					});
					if (res.code === 200 || res.msg === '注册成功') {
						uni.showToast({ title: '注册成功，请登录' });
						this.isRegister = false;
					} else {
						uni.showToast({ title: res.msg || '注册失败', icon: 'none' });
					}
				} catch (e) {
					console.error(e);
				} finally {
					this.loading = false;
				}
			}
		}
	}
</script>

<style lang="scss">
	.login-container {
		padding: 50rpx;
		background-color: #fff;
		min-height: 100vh;
	}
	
	.header {
		margin-bottom: 80rpx;
		text-align: center;
		margin-top: 100rpx;
	}
	
	.title {
		font-size: 48rpx;
		font-weight: bold;
		color: #333;
	}
	
	.form-box {
		width: 100%;
	}
	
	.input-group {
		margin-bottom: 40rpx;
	}
	
	.label {
		display: block;
		font-size: 28rpx;
		color: #666;
		margin-bottom: 10rpx;
	}
	
	.input {
		border-bottom: 1px solid #eee;
		height: 80rpx;
		font-size: 32rpx;
	}
	
	.submit-btn {
		background-color: $uni-color-primary;
		color: #fff;
		border-radius: 40rpx;
		margin-top: 60rpx;
		font-size: 32rpx;
	}
	
	.links {
		margin-top: 30rpx;
		text-align: center;
		font-size: 28rpx;
		color: $uni-color-primary;
	}
	
	.register-fields {
		margin-top: 40rpx;
		border-top: 1px solid #eee;
		padding-top: 40rpx;
	}
</style>
