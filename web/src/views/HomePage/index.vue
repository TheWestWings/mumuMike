<template>
  <div class="cover-container">
    <router-link to="/Shopping">
      <el-button type="primary">去购买</el-button>
    </router-link>
    <span>&ensp;</span>
    <router-link to="/MgmtPage">
      <el-button type="primary">去管理</el-button>
    </router-link>
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
  background-size: 100% 100%;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>