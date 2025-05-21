<template>
  <div class="cover-container">
    <div class="overlay">
      <div class="button-container">
        <router-link to="/Shopping">
          <el-button class="custom-button">
            <span class="text">去购买</span>
            <span class="arrow">⇀</span>
          </el-button>
        </router-link>
        <router-link to="/MgmtPage" v-if="$store.state.role === '0' || $store.state.role === '1'">
            
          <el-button class="custom-button">
            <span class="text">去管理</span>
            <span class="arrow">⇀</span>
          </el-button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>

<style scoped>
.cover-container {
  height: 100vh;
  width: 100%;
  background-image: url('@/assets/background/login1.png');
  background-size: cover;
  background-position: center;
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  right: 0;
  width: 500px;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.custom-button {
  width: 300px;
  height: 70px;
  font-size: 28px;
  border-radius: 15px;
  background: transparent;
  border: 2px solid rgba(0, 0, 0, 0.6);
  color: rgb(212, 198, 198);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.3);
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.text {
  position: relative;
  z-index: 1;
  margin-bottom: 5px;
}

.arrow {
  position: absolute;
  bottom: 2px;
  left: 50%;
  font-weight: 1000;
  transform: translateX(-50%) translateY(20px) scaleX(7);
  font-size: 24px;
  color: rgb(105, 70, 37);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  width: auto;
  transform-origin: center;
}

.custom-button:hover {
  transform: translateY(-2px);
  color: rgb(212, 198, 198);
  text-shadow: 0 4px 12px rgba(105, 70, 37, 0.4),
              0 2px 4px rgba(105, 70, 37, 0.3);
  letter-spacing: 1px;
  border-color: rgba(0, 0, 0, 0.8);
}

.custom-button:hover .arrow {
  opacity: 1;
  transform: translateX(-50%) translateY(0) scaleX(4);
}

.custom-button:active {
  transform: translateY(0);
  text-shadow: 0 2px 4px rgba(105, 70, 37, 0.2);
}

.custom-button:active .arrow {

  transform: translateX(-50%) translateY(2px) scaleX(8);
}
/* 响应式布局 */
@media screen and (max-width: 768px) {
  .overlay {
    width: 100%;
    background: linear-gradient(to bottom,
      rgba(0, 0, 0, 0) 0%,
      rgba(0, 0, 0, 0.3) 50%,
      rgba(0, 0, 0, 0.5) 100%
    );
  }

  .button-container {
    width: 100%;
    padding: 0 20px;
    gap: 20px;
    margin-top: auto;
    margin-bottom: 60px;
  }

  .custom-button {
    width: 100%;
    max-width: 280px;
    height: 60px;
    font-size: 24px;
  }

  .arrow {
    font-size: 20px;
    transform: translateX(-50%) translateY(20px) scaleX(5);
  }

  .custom-button:hover .arrow {
    transform: translateX(-50%) translateY(0) scaleX(6);
  }

  .custom-button:active .arrow {
    transform: translateX(-50%) translateY(2px) scaleX(6);
  }
}

@media screen and (max-width: 480px) {
  .custom-button {
    height: 50px;
    font-size: 20px;
    max-width: 240px;
  }

  .button-container {
    margin-bottom: 40px;
  }

  .arrow {
    font-size: 18px;
    transform: translateX(-50%) translateY(15px) scaleX(4);
  }

  .custom-button:hover .arrow {
    transform: translateX(-50%) translateY(0) scaleX(5);
  }

  .custom-button:active .arrow {
    transform: translateX(-50%) translateY(2px) scaleX(5);
  }
}
</style>