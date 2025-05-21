<template>
  <div class="cover-container">
    <div class="content-wrapper">
      <div class="branding">
        <h1 class="main-title">木木奶茶</h1>
        <p class="tagline">清凉一夏，品味生活</p>
      </div>
      <div class="overlay">
        <div class="button-container">
          <router-link to="/Shopping">
            <el-button class="custom-button primary-btn">
              <span class="text">去购买</span>
              <span class="icon">
                <i class="el-icon-shopping-cart-2"></i>
              </span>
            </el-button>
          </router-link>
          <router-link to="/MgmtPage/UsersMgmt" v-if="role === 0 || role === 1">
            <el-button class="custom-button secondary-btn">
              <span class="text">去管理</span>
              <span class="icon">
                <i class="el-icon-setting"></i>
              </span>
            </el-button>
          </router-link>
          <el-button class="custom-button logout-btn" v-if="isLoggedIn" @click="handleLogout">
            <span class="text">退出登录</span>
            <span class="icon">
              <i class="el-icon-switch-button"></i>
            </span>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  created() {
    this.role = this.$store.state.role;
    this.role = parseInt(this.role);
  },
  data(){
    return {
      role : this.$store.state.role
    }
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.token && this.$store.state.token !== '';
    }
  },
  methods: {
    handleLogout() {
      this.$store.commit('clearAll')
      this.$store.commit('car/clearCarList')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap');

.cover-container {
  height: 100vh;
  width: 100%;
  background-image: url('@/assets/background/login1.png');
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

.cover-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.1) 0%,
    rgba(0, 0, 0, 0.4) 100%
  );
  z-index: 1;
}

.content-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  z-index: 2;
}

.branding {
  position: absolute;
  top: 15%;
  left: 10%;
  z-index: 3;
  animation: fadeInUp 1s ease-out;
}

.main-title {
  font-family: 'Poppins', sans-serif;
  font-size: 4.5rem;
  font-weight: 700;
  color: #fff;
  margin: 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  letter-spacing: 2px;
}

.tagline {
  font-family: 'Poppins', sans-serif;
  font-size: 1.8rem;
  font-weight: 300;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 10px;
  text-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
  letter-spacing: 1px;
}

.overlay {
  position: absolute;
  top: 0;
  right: 0;
  width: 500px;
  height: 100%;
  background: linear-gradient(
    to right,
    rgba(0, 0, 0, 0) 0%,
    rgba(0, 0, 0, 0.6) 100%
  );
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  animation: slideInRight 0.8s ease-out;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 35px;
  padding: 30px;
}

.custom-button {
  width: 320px;
  height: 70px;
  font-size: 1.5rem;
  font-family: 'Poppins', sans-serif;
  border-radius: 50px;
  background: transparent;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  letter-spacing: 1px;
  font-weight: 500;
}

.primary-btn {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.05) 100%);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #fff;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.secondary-btn {
  background: linear-gradient(135deg, rgba(105, 70, 37, 0.1) 0%, rgba(105, 70, 37, 0.3) 100%);
  border: 1px solid rgba(105, 70, 37, 0.4);
  color: #fff;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.logout-btn {
  background: linear-gradient(135deg, rgba(230, 76, 76, 0.1) 0%, rgba(230, 76, 76, 0.3) 100%);
  border: 1px solid rgba(230, 76, 76, 0.4);
  color: #fff;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.custom-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.2) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 0;
}

.text {
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
  display: inline-block;
}

.icon {
  position: relative;
  z-index: 1;
  margin-left: 8px;
  opacity: 0;
  display: inline-block;
  transition: all 0.3s ease;
  vertical-align: middle;
}

.custom-button:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.3);
}

.custom-button:hover::before {
  left: 100%;
}

.custom-button {
  position: relative;
}

.custom-button span {
  display: inline-block;
  vertical-align: middle;
}

.custom-button:hover .text {
  transform: translateX(0);
}

.custom-button:hover .icon {
  opacity: 1;
  transform: translateX(0);
}

.custom-button:active {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}

.primary-btn:hover {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%);
}

.secondary-btn:hover {
  background: linear-gradient(135deg, rgba(105, 70, 37, 0.2) 0%, rgba(105, 70, 37, 0.4) 100%);
}

.logout-btn:hover {
  background: linear-gradient(135deg, rgba(230, 76, 76, 0.2) 0%, rgba(230, 76, 76, 0.4) 100%);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .main-title {
    font-size: 3.5rem;
  }
  
  .tagline {
    font-size: 1.5rem;
  }
  
  .overlay {
    width: 450px;
  }
  
  .custom-button {
    width: 300px;
  }
}

/* 平板端优化 */
@media screen and (max-width: 768px) {
  .cover-container::before {
    background: linear-gradient(
      to bottom,
      rgba(0, 0, 0, 0.2) 0%,
      rgba(0, 0, 0, 0.5) 100%
    );
  }
  
  .branding {
    top: 8%;
    left: 50%;
    transform: translateX(-50%);
    text-align: center;
    width: 100%;
    padding: 0 20px;
  }
  
  .main-title {
    font-size: 3rem;
    text-shadow: 0 2px 15px rgba(0, 0, 0, 0.5);
  }
  
  .tagline {
    font-size: 1.3rem;
    margin-top: 5px;
  }

  .overlay {
    position: fixed;
    bottom: 0;
    left: 0;
    top: auto;
    right: auto;
    width: 100%;
    height: auto;
    background: linear-gradient(to top,
      rgba(0, 0, 0, 0.8) 0%,
      rgba(0, 0, 0, 0.5) 70%,
      rgba(0, 0, 0, 0) 100%
    );
    backdrop-filter: blur(8px);
    animation: slideInUp 0.8s ease-out;
  }

  .button-container {
    width: 100%;
    padding: 30px 20px;
    gap: 25px;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    margin: 0;
  }

  .custom-button {
    width: 180px;
    min-width: 180px;
    height: 60px;
    font-size: 1.2rem;
    border-radius: 15px;
  }
  
  .primary-btn, .secondary-btn {
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.4);
    color: #fff;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.3);
  }
  
  .custom-button:hover {
    transform: translateY(-3px);
    background: rgba(255, 255, 255, 0.25);
  }
  
  .icon {
    margin-left: 10px;
  }
  
  @keyframes slideInUp {
    from {
      opacity: 0;
      transform: translateY(50px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
}

/* 手机端优化 */
@media screen and (max-width: 480px) {
  .branding {
    top: 10%;
  }
  
  .main-title {
    font-size: 2.3rem;
  }
  
  .tagline {
    font-size: 1rem;
    margin-top: 0;
  }
  
  .overlay {
    background: linear-gradient(to top,
      rgba(0, 0, 0, 0.85) 0%,
      rgba(0, 0, 0, 0.6) 60%,
      rgba(0, 0, 0, 0) 100%
    );
  }
  
  .button-container {
    flex-direction: column;
    padding: 25px 20px;
    gap: 20px;
    align-items: center;
  }
  
  .custom-button {
    width: 280px;
    height: 60px;
    font-size: 1.1rem;
    border-radius: 30px;
  }
  
  .custom-button::before {
    display: block; /* 恢复光效 */
  }
  
  .primary-btn {
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.25);
  }
  
  .secondary-btn {
    background: linear-gradient(135deg, rgba(105, 70, 37, 0.15) 0%, rgba(105, 70, 37, 0.35) 100%);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.25);
  }
  
  .logout-btn {
    background: linear-gradient(135deg, rgba(230, 76, 76, 0.15) 0%, rgba(230, 76, 76, 0.35) 100%);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.25);
  }
  
  /* 针对移动设备的触摸优化 */
  .custom-button:hover {
    transform: translateY(-3px);
  }
  
  .custom-button:active {
    transform: translateY(0);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
    transition: all 0.1s;
  }
  
  .icon {
    margin-left: 10px;
  }
}

/* 小型手机设备 */
@media screen and (max-width: 360px) {
  .main-title {
    font-size: 2rem;
  }
  
  .tagline {
    font-size: 0.9rem;
  }
  
  .custom-button {
    width: 90%;
    height: 48px;
    font-size: 1rem;
    max-width: 220px;
  }
  
  .button-container {
    padding: 20px;
    gap: 15px;
  }
}

/* 竖屏平板 */
@media screen and (min-width: 481px) and (max-width: 768px) and (orientation: portrait) {
  .overlay {
    padding-bottom: env(safe-area-inset-bottom, 0);
  }
  
  .button-container {
    padding-bottom: calc(20px + env(safe-area-inset-bottom, 0));
  }
}

/* 横屏手机 */
@media screen and (max-height: 480px) and (orientation: landscape) {
  .branding {
    top: 5%;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .tagline {
    font-size: 0.9rem;
  }
  
  .overlay {
    height: auto;
  }
  
  .button-container {
    flex-direction: row;
    padding: 15px;
    gap: 15px;
  }
  
  .custom-button {
    height: 45px;
    width: 40%;
    font-size: 1rem;
  }
}
</style>