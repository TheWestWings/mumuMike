<template>
<el-container class="mgmt-container">
  <el-header>
    <header class="header">
      <div class="header-left">
        <i class="el-icon-s-unfold mobile-menu-toggle" @click="toggleMenu"></i>
      </div>
      <div class="header-content">
        <h1>木木牛奶公司</h1>
        <p>用心制作的奶香甜品</p>
      </div>
      <el-button
        class="back-btn"
        type="text"
        icon="el-icon-back"
        @click="$router.push('/HomePage')">
        <span class="btn-text">返回首页</span>
      </el-button>
    </header>
  </el-header>
  <el-container>
    <div class="sidebar-overlay" v-if="isMobile && sidebarOpen" @click="closeSidebarOnMobile"></div>
    <el-aside :width="sidebarWidth" :class="{'mobile-sidebar': isMobile, 'sidebar-open': sidebarOpen}">
      <div class="sidebar-header">
        <i class="el-icon-s-operation"></i>
        <span>系统管理</span>
      </div>
      <el-menu
        class="el-menu-vertical-demo"
        background-color="#f9f0ef"
        text-color="#303133"
        active-text-color="#7b5e55"
        router>
        
        <el-menu-item index="/MgmtPage/UsersMgmt" @click="closeSidebarOnMobile">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>

        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-s-order"></i>
            <span>订单管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/MgmtPage/OrderMgmt" @click="closeSidebarOnMobile">未完成订单</el-menu-item>
            <el-menu-item index="/MgmtPage/HistoryOrder" @click="closeSidebarOnMobile">已完成订单</el-menu-item>
          </el-menu-item-group>
        </el-submenu>


        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-goods"></i>
            <span>产品管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/MgmtPage/SeriesMgmt" @click="closeSidebarOnMobile">类别管理</el-menu-item>
            <el-menu-item index="/MgmtPage/ProductsMgmt" @click="closeSidebarOnMobile">产品管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>

        <el-menu-item index="/MgmtPage/InformationMgmt" @click="closeSidebarOnMobile">
          <i class="el-icon-chat-line-square"></i>
          <span slot="title">留言管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <div class="main-content">
        <router-view></router-view>
      </div>
    </el-main>
  </el-container>
</el-container>
</template>

<script>
export default {
  name: 'MgmtPage',
  data() {
    return {
      sidebarOpen: false,
      isMobile: false,
      sidebarWidth: '220px'
    };
  },
  created() {
    this.checkScreenSize();
    window.addEventListener('resize', this.checkScreenSize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkScreenSize);
  },
  methods: {
    toggleMenu() {
      this.sidebarOpen = !this.sidebarOpen;
      if (this.isMobile) {
        this.sidebarWidth = this.sidebarOpen ? '220px' : '0px';
        // 当打开侧边栏时，禁止页面滚动
        if (this.sidebarOpen) {
          document.body.style.overflow = 'hidden';
        } else {
          document.body.style.overflow = '';
        }
      }
    },
    checkScreenSize() {
      this.isMobile = window.innerWidth <= 768;
      if (this.isMobile) {
        this.sidebarWidth = this.sidebarOpen ? '220px' : '0px';
      } else {
        this.sidebarWidth = '220px';
        this.sidebarOpen = true;
      }
    },
    closeSidebarOnMobile() {
      if (this.isMobile) {
        this.sidebarOpen = false;
        this.sidebarWidth = '0px';
        document.body.style.overflow = '';
      }
    }
  }
};
</script>

<style scoped>
/* 基本布局 */
.mgmt-container {
  min-height: 100vh;
  background-color: #f9f6f2;
}

.el-container .el-container {
  flex-direction: row;
  flex: 1;
}

/* 移动端显示处理 */
.header-left {
  display: none;
}

.mobile-menu-toggle {
  color: white;
  font-size: 24px;
  cursor: pointer;
}

.el-header {
  padding: 0;
  height: 120px !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 10;
  position: relative;
}

.header {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: linear-gradient(135deg, rgba(93, 64, 55, 0.9), rgba(139, 110, 99, 0.8));
}

.header-content {
  text-align: center;
  color: #fff;
}

.header h1 {
  font-size: 2.5em;
  margin: 0 0 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  letter-spacing: 1px;
  font-weight: 600;
}

.header p {
  margin: 0;
  font-size: 1.1em;
  letter-spacing: 1px;
}

.back-btn {
  color: #fff;
  font-size: 15px;
  padding: 10px 15px;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  white-space: nowrap;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.el-aside {
  background-color: #f9f0ef;
  border-right: 1px solid #e6e6e6;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  padding-top: 10px;
  overflow-x: hidden;
  z-index: 9;
}

.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 8;
}

.mobile-sidebar {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  height: 100vh;
}

.sidebar-open {
  transform: translateX(0);
}

.sidebar-header {
  padding: 15px 20px;
  font-size: 16px;
  font-weight: bold;
  color: #7b5e55;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(139, 110, 99, 0.2);
  margin-bottom: 10px;
}

.sidebar-header i {
  margin-right: 8px;
  font-size: 18px;
}

.el-main {
  background-color: #f9f6f2;
  overflow: visible;
  height: auto;
  padding: 25px;
}

.main-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  min-height: calc(100vh - 180px);
}

.el-menu {
  border-right: none;
}

.el-menu-item {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 50px;
  line-height: 50px;
  transition: all 0.3s;
  border-radius: 0 20px 20px 0;
  margin: 5px 0;
  width: 94%;
}

.el-menu-item:hover {
  background-color: rgba(139, 110, 99, 0.1) !important;
}

.el-menu-item.is-active {
  background-color: rgba(139, 110, 99, 0.15) !important;
  color: #7b5e55 !important;
  font-weight: bold;
}

.el-submenu__title {
  height: 50px;
  line-height: 50px;
}

.el-submenu__title:hover {
  background-color: rgba(139, 110, 99, 0.1) !important;
}

.el-menu-item-group__title {
  padding: 0;
}

a {
  text-decoration: none;
}

/* 响应式布局 */
@media screen and (max-width: 992px) {
  .header h1 {
    font-size: 2em;
  }
  
  .header p {
    font-size: 1em;
  }
  
  .el-main {
    padding: 20px;
  }
}

@media screen and (max-width: 768px) {
  .el-container .el-container {
    flex-direction: column;
  }
  
  .el-header {
    height: 100px !important;
  }
  
  .header {
    padding: 0 10px;
  }
  
  .mobile-sidebar {
    top: 0;
    height: 100%;
    padding-top: 100px; /* 与header高度一致 */
  }
  
  .header-left {
    display: block;
    flex: 0 0 40px;
  }
  
  .header-content {
    flex: 1;
  }
  
  .header h1 {
    font-size: 1.6em;
    margin-bottom: 5px;
  }
  
  .header p {
    font-size: 0.9em;
  }
  
  .btn-text {
    display: none;
  }
  
  .back-btn {
    padding: 8px;
    border-radius: 50%;
    min-width: 36px;
    min-height: 36px;
  }
  
  .el-aside {
    width: 0 !important;
    transform: translateX(-100%);
  }
  
  .sidebar-open {
    width: 220px !important;
    transform: translateX(0);
  }
  
  .sidebar-header span {
    display: inline-block;
  }
  
  .el-main {
    padding: 15px;
    width: 100%;
  }
  
  .main-content {
    padding: 15px;
  }
}

@media screen and (max-width: 480px) {
  .el-header {
    height: 80px !important;
  }
  
  .mobile-sidebar {
    padding-top: 80px; /* 更新为与小屏幕header高度一致 */
  }
  
  .header h1 {
    font-size: 1.4em;
  }
  
  .header p {
    font-size: 0.8em;
  }
  
  .el-main {
    padding: 10px;
  }
  
  .main-content {
    padding: 10px;
    min-height: calc(100vh - 120px);
  }
  
  .el-menu-item {
    padding: 0 10px;
  }
}
</style>