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
  
      </div>
  
  
      <round-button     
      :buttonType="'注册'"
      @click="submit"
      class="auth-button"
      ></round-button>
  
      <div class="auth-footer">
      <span>已有账号？</span>
      <a href="#" @click.prevent="dispLogin">立即登录</a>
      </div>
      </el-form>
  </div>
  </template>
  
  <script>
  import axios from 'axios'
  import RoundButton from './roundButton.vue'
  import FaildAlert from './faildAlert.vue'
  
  
  
  
  export default {
    components: {RoundButton, FaildAlert},
      methods: {
        dispLogin() {
          this.$emit('change', false) 
        },
        submit() {
          this.$refs.regForm.validate((valid) => {
            if(valid){
              console.log("can");
              this.register();
  
            } else {
              this.handleAlert("注册失败，请修改完善注册信息！!");
              return false;
            }
          })
        },
  
  
        register() {
          axios({
            url:'http://localhost:8080/register',
            method: 'POST',
  
            data: {
              username: this.regForm.regUsername,
              email: this.regForm.regEmail,
              phone: this.regForm.regPhone,
              password: this.regForm.regPswd,
            }
          }).then(result => {
            let judge = result.data.code
          
            if(judge === 200) {
              console.log(result.data.msg)
      
              this.$emit('success',this.regForm.regUsername, this.regForm.regPswd)
              this.dispLogin()
            
  
  
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
   
             },
            regForm :{
              regUsername: '',
              regEmail: '',
              regPhone: '',
              regConfirmPswd: '',
              regPswd: '',
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
    color: #3498db;
    text-decoration: none;
    margin-left: 5px;
    font-weight: 500;
    transition: all 0.2s;
    position: relative;
  }
  
  .auth-footer a:hover {
    color: #2980b9;
    text-shadow: 0 0 8px rgba(52, 152, 219, 0.3);
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
  </style>