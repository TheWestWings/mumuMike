<template>
  <el-container>
    <router-view></router-view>
  </el-container>
</template>

<script>
export default {
  name: "LoginPage"
}
</script>

<style scoped>
.el-container {
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