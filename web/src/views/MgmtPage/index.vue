<template>
<el-container>
  <el-header>
    <header class="header" >
        <div class="header-content">
            <h1>木木牛奶公司</h1>
            <p>用心制作的奶香甜品</p>
        </div>
      </header>
  </el-header>
  <el-button @click="$router.push('/HomePage')">返回首页</el-button>
  <el-container>


    <el-aside width="200px">
      <el-menu
      
      class="el-menu-vertical-demo"

      router>
      
      <el-menu-item index="/MgmtPage/UsersMgmt">
        <i class="el-icon-menu"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>

      <el-submenu index="">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>订单管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/MgmtPage/OrderMgmt">未完成订单</el-menu-item>
          <el-menu-item index="/MgmtPage/HistoryOrder">已完成订单</el-menu-item>
        </el-menu-item-group>
      </el-submenu>



      <el-submenu index="">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>产品管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/MgmtPage/SeriesMgmt">类别</el-menu-item>
          <el-menu-item index="/MgmtPage/ProductsMgmt">具体产品</el-menu-item>
        </el-menu-item-group>
      </el-submenu>




      <el-menu-item index="/MgmtPage/MessageMgmt">
        <i class="el-icon-setting"></i>
        <span slot="title">留言管理</span>
      </el-menu-item>
    </el-menu>
    </el-aside>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
 
</template>

<script>
export default {
  name: 'MgmtPage'

}
</script>

<style scoped>
.el-container {
  height: 100vh;
}

.el-container .el-container {
  flex-direction: row;
  flex: 1;
}

.el-header {
  padding: 0;
  height: 120px !important;
}

.header {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(93, 64, 55, 0.8);
}

.header-content {
  text-align: center;
  color: #fff;
}

.header h1 {
  font-size: 3em;
  margin: 0 0 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.header p {
  margin: 0;
}

.el-aside {
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.el-main {
  background-color: #f5f7fa;
}

.el-menu {
  border-right: none;
}

.el-menu-item {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

a {
  text-decoration: none;
}
</style>