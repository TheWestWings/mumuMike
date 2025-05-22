<template>
  <div class="auth-box" >
  
      <h1 class="auth-title">用户注册</h1>
  
      <div class="alert-area">
          <faild-alert
          v-if="isFailed"
              :title="errorType"
          ></faild-alert>
          
  
      </div>
  
      <el-form :rules="regRule" :model="regForm" ref="regForm">
        <div class="input-group">
          <el-form-item label="用户名" prop="regUsername">
            <el-input 
              v-model="regForm.regUsername" 
              placeholder="请输入用户名"
              clearable
            ></el-input>
          </el-form-item>
  
          <el-form-item label="邮箱" prop="regEmail">
            <el-input 
              v-model="regForm.regEmail" 
              placeholder="请输入邮箱地址"
              clearable
            ></el-input>
          </el-form-item>
  
          <el-form-item label="电话" prop="regPhone">
            <el-input 
              v-model="regForm.regPhone" 
              placeholder="请输入电话号码"
              clearable
            ></el-input>
          </el-form-item>
  
          <el-form-item  label="密码" prop="regPswd">
            <el-input
             v-model="regForm.regPswd"
              type="password"
              placeholder="请输入密码"
              show-password
              autocomplete="new-password"
            ></el-input>
          </el-form-item>
  
          <el-form-item label="确认密码" prop="regConfirmPswd">
            <el-input
              v-model="regForm.regConfirmPswd"
              type="password"
              placeholder="请确认密码"
              show-password
            ></el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="captcha">
            <div class="captcha-container">
              <el-input
                v-model="regForm.captcha"
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
      :buttonType="'注册'"
      @click="submit"
      class="auth-button"
      ></round-button>
  
      <div class="auth-footer">
      <span>已有账号？</span>
      <router-link to="/auth/login">立即登录</router-link>
      </div>
      </el-form>
  </div>
  </template>
  
  <script>
  import FaildAlert from "@/components/faildAlert.vue"
  import RoundButton from '@/components/roundButton.vue'
import { register } from '@/api/User/User'
  
  
  
  export default {
    components: {RoundButton, FaildAlert},
    
    mounted() {
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
        this.regForm.captcha = '';
      },
  
      submit() {
        this.$refs.regForm.validate((valid) => {
          if(valid){
            console.log("can");
            this.register();
  
          } else {
            this.handleAlert("注册失败，请修改完善注册信息！");
            return false;
          }
        })
      },
  
  
        register() {
            register({
                username: this.regForm.regUsername,
              email: this.regForm.regEmail,
              phone: this.regForm.regPhone,
              password: this.regForm.regPswd,
            }).then(result => {
            let judge = result.data.code
          
            if(judge === 200) {
              console.log(result.data.msg)
      
              this.$store.commit('setUsername', this.regForm.regUsername)
              this.$store.commit('setPswd', this.regForm.regPswd)
              this.$store.commit('setRole', 2)
              this.$store.commit('setEmail', this.regForm.regEmail)
              this.$store.commit('setPhone', this.regForm.regPhone)


              this.$router.push('/auth/login')
            
  
  
            } else if(judge === 403) {
              console.log(result.data.msg)
                this.handleAlert("注册失败，用户名已存在！")
                // this.regForm.regUsername = ''
            } else {
              this.handleAlert("注册失败，请联系管理员！")
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
  
      },
      data() {
          return {
            regRule: {
              regUsername: [
                { required: true, message: '请输入用户名', trigger: 'blur'},
                { min: 4, max: 16, message: '长度在4到16个字符', trigger: 'blur' },
                { pattern: /^[a-zA-Z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
              ],
              regEmail: [
                { required: true, message: '请输入邮箱地址', trigger: 'blur'},
                { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
              ],
              regPhone: [
                { required: true, message: '请输入电话号码', trigger: 'blur'},
                { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }
              ],
              regPswd: [
                { required: true, message: '请输入密码', trigger: 'blur'},
                { min: 6, max: 20, message: '长度在6到20个字符', trigger: 'blur' },
                { pattern: /^(?=.*[a-zA-Z])(?=.*\d).+$/, message: '必须包含字母和数字', trigger: 'blur' }
              ],
              regConfirmPswd: [
                { required: true, message: '请确认密码', trigger: 'blur'},
                { validator: (rule, value, callback) => {
                  if(value !== this.regForm.regPswd){
                    callback(new Error('两次输入密码不一致!'));
                  } else {
                    callback();
                  }
                }, trigger: 'blur'}
  
              ],
              
              captcha: [
                { required: true, message: '请输入验证码', trigger: 'blur'},
                { validator: (rule, value, callback) => {
                  if(value === ''){
                    callback(new Error('请输入验证码!'));
                  } else if(value.toLowerCase() !== this.correctCaptcha.toLowerCase()){
                    callback(new Error('验证码错误!'));
                  } else {
                    callback();
                  }
                }, trigger: 'blur'}
              ],
   
             },
            captchaText: '',
            correctCaptcha: '',
            
            regForm :{
              regUsername: '',
              regEmail: '',
              regPhone: '',
              regConfirmPswd: '',
              regPswd: '',
              captcha: '',
            },
  
            errors: {
              loginError: '用户名或密码不正确',
              regUsername: '用户名长度应大于6字符',
              regPswd: '密码长度应大于8个字符',
              regConfirmdPswd: '确认密码与密码不一致',
              regPhone: '电话号码不正确 ',
              regEmail: '邮箱地址不正确',
            },
  
            registerErrorMsg: '',
            isFailed: false,
            isSuccess: false,
            errorType: '',
  
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