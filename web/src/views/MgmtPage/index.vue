<template>
  <el-container>
    <el-header style="height: 450px;">
      <header class="header" >
        <div class="header-content">
            <h1>木木牛奶公司</h1>
            <p>用心制作的奶香甜品</p>
        </div>
      </header>

      <el-menu
        :default-active="activeIndex2"
        class="nav1"
        mode="horizontal"

        background-color="#8d6e63"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-menu-item index="1"><a href="#home">用户管理</a></el-menu-item>
        <el-menu-item index="2"><a href="#sugar">订单管理</a></el-menu-item>
        <el-menu-item index="3"><a href="#snow">产品管理</a></el-menu-item>
      </el-menu>
    </el-header>

    </el-container>

</template>

<script>
export default {
  name: 'MgmtPage'

}
</script>

<style scoped>
.header {
    height: 400px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(93, 64, 55, 0.8);
}

.header-content {
    text-align: center;
    color: #fff;
    position: relative;
    z-index: 2;
}

.header h1 {
    font-size: 3em;
    margin-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

a {
  text-decoration: none;
}

.nav1 {
  display: flex !important;
  justify-content: center !important;
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>