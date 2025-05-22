<template>
  <div class="auth-box" >
    <h1 class="auth-title">用户登录</h1>
      <div class="alert-area">
          <faild-alert
              v-if="isFailed"
              :title="errorType"
          ></faild-alert>
        </div>

    <el-form :model="form" :rules="rule" ref="form">
      <div class="input-group">
        <el-form-item label = '用户名' prop="username">
          <el-input
            v-model="form.username"
            ref="username"
            placeholder="请输入用户名"
            clearable
            ></el-input>
        </el-form-item>

        <el-form-item label = '密码' prop="pswd">
            <el-input
              ref="pswd"
              v-model="form.pswd"
              placeholder="请输入密码"
              show-password
              autocomplete="new-password"
              ></el-input>
        </el-form-item>

        <el-form-item label = '验证码' prop="captcha">
          <div class="captcha-container">
            <el-input
              ref="captcha"
              v-model="form.captcha"
              placeholder="请输入验证码"
              class="captcha-input"
              ></el-input>
            <div class="captcha-image" @click="refreshCaptcha">
              {{ captchaText }}
            </div>
          </div>
        </el-form-item>
      </div>
      <round-button     
      :buttonType="'登录'"
      @click="submit"
      class="auth-button"
      ></round-button>

      <div class="auth-footer">
        <span>还没有账号？</span>
        <router-link to="/auth/register">立即注册</router-link>
        
      </div>
    </el-form>
  </div>

</template>

<script>

import faildAlert from "@/components/faildAlert.vue"
import RoundButton from '@/components/roundButton.vue'
import { getInfo, login } from '@/api/User/User'



export default {
components: { faildAlert, RoundButton,  },

mounted() {
  // this.form.username = this.$store.state.username
  // this.form.pswd = this.$store.state.psw
  // this.form.username = 'zzl'
  // this.form.pswd = '123'
  this.generateCaptcha();
},

  methods: {
    generateCaptcha() {
      // 生成随机验证码
      const characters = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
      let result = '';
      for (let i = 0; i < 4; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
      }
      this.captchaText = result;
      this.correctCaptcha = result;
    },

    refreshCaptcha() {
      this.generateCaptcha();
      this.form.captcha = '';
    },

    submit() {
      this.$refs.form.validate((valid) => {
        if(valid){
          console.log("can");
          this.login();

        } else {
          this.handleAlert("登录失败，请完整填写用户名、密码与验证码!");
          return false;
        }
      })
    },

    login() {
        login({
          username: this.form.username,
          password: this.form.pswd
        }).then(result => {
        
          let judge = result.data.code
          

          if(judge === 200) {

            this.$store.commit('setToken', result.data.data)
            console.log(this.$store.state.token)

            getInfo().then(res => {
              const user = res.data.data
              this.$store.commit('setId', user.id)
              this.$store.commit('setUsername', user.username)
              this.$store.commit('setRole', user.role)
              this.$store.commit('setEmail', user.email)
              this.$store.commit('setPhone', user.phone)
              this.$store.commit('setAvatar', user.avatar)
              this.welcome()
    
            })

            
          this.isSuccess = true
          
          } else {

            this.handleAlert("登录失败，请检查用户名或密码")
            this.form.pswd = ''
          }
        })
      },
      handleAlert(cue) {
        this.errorType = cue
            this.isFailed = true

            setTimeout(() => {
              this.isFailed = false;
            }, 2000);
      },

      welcome() {
        console.log('welcome')
        let now = this.$route.query.redirect
        console.log('now ' + now)
        if(!now) now = '/HomePage'
        this.$router.push({
          path: now,  
        });
      }
  },
  data() {
      return {

        form: {
          username: '',
          pswd: '',
          captcha: '',
        },
        
        captchaText: '',
        correctCaptcha: '',

        rule: {
          username: [
            { validator: (rule, value, callback) => {
                if(value === ''){
                  callback(new Error('请输入用户名!'));
                } else {
                  callback();
                }
              }, trigger: 'blur'}

          ],
          pswd: [
            { validator: (rule, value, callback) => {
                  if(value === ''){
                    callback(new Error('请输入密码!'));
                  } else {
                    callback();
                  }
                }, trigger: 'blur'}
            
          ],
          captcha: [
            { validator: (rule, value, callback) => {
                  if(value === ''){
                    callback(new Error('请输入验证码!'));
                  } else if(value.toLowerCase() !== this.correctCaptcha.toLowerCase()){
                    callback(new Error('验证码错误!'));
                  } else {
                    callback();
                  }
                }, trigger: 'blur'}
          ]
        },
        buttonType: '登录',
        correct: '登录成功',
        isFailed: false,  // 初始不显示
        errorType: '',    // 初始无错误信息
      }
  }

}
</script>

<style scoped>
.auth-box {
background: rgba(255, 255, 255, 0.78);
backdrop-filter: blur(4px);
-webkit-backdrop-filter: blur(4px);
padding: 40px;
border-radius: 16px;
border: 1px solid rgba(255,255,255,0.25);
transition: all 0.3s ease;
}

.auth-title {
text-align: center;
color: #2c3e50;
margin-bottom: 30px;
font-size: 28px;
font-weight: 600;
text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.input-group {
margin-bottom: 25px;
}

.input-group > * {
margin-bottom: 20px;
}

.auth-button {
display: block;
margin: 10px auto 0;
width: fit-content;
}

.auth-button >>> .el-button {
width: 280px;
}

.auth-footer {
text-align: center;
margin-top: 20px;
color: rgba(0, 0, 0, 0.7);
font-size: 14px;
}

.auth-footer a {
color: #8d6e63;
text-decoration: none;
margin-left: 5px;
font-weight: 500;
transition: all 0.2s;
position: relative;
}

.auth-footer a:hover {
color: #a37f72;
text-shadow: 0 0 8px rgba(179, 82, 30, 0.3);
}

.auth-footer a::after {
content: '';
position: absolute;
bottom: -2px;
left: 0;
width: 100%;
height: 1px;
background: currentColor;
transform: scaleX(0);
transition: transform 0.3s;
}

.auth-footer a:hover::after {
transform: scaleX(1);
}

/* 夜间模式 */
@media (prefers-color-scheme: dark) {
.auth-box {
  background: rgba(30, 30, 30, 0.85);
  border-color: rgba(255,255,255,0.1);
}

.auth-title {
  color: #ecf0f1;
}

.auth-footer {
  color: rgba(255, 255, 255, 0.7);
}
}

/* 动画效果 */
@keyframes fadeIn {
from { opacity: 0; transform: translateY(20px); }
to { opacity: 1; transform: translateY(0); }
}

@keyframes slideDown {
from { opacity: 0; transform: translateY(-10px); }
to { opacity: 1; transform: translateY(0); }
}

/* 响应式设计 */
@media (max-width: 480px) {

.auth-box {
  padding: 30px 20px;
  border-radius: 12px;
}

.auth-title {
  font-size: 24px;
  margin-bottom: 25px;
}


}

/* 验证码样式 */
.captcha-container {
  display: flex;
  align-items: center;
}

.captcha-input {
  width: 65%;
  margin-right: 10px;
}

.captcha-image {
  width: 35%;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background: #f2f6fc;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
  font-weight: bold;
  font-size: 20px;
  letter-spacing: 3px;
  color: #606266;
  cursor: pointer;
  user-select: none;
  position: relative;
  overflow: hidden;
}

.captcha-image::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, transparent 45%, rgba(255,255,255,0.8) 50%, transparent 55%);
  background-size: 200% 200%;
  animation: shine 3s infinite;
}

@keyframes shine {
  0% { background-position: 100% 100%; }
  100% { background-position: 0% 0%; }
}

.captcha-image:hover {
  background-color: #e6e6e6;
}

</style>