<template>
  <div class="cover-container">
    <el-button>去购买</el-button>
    <el-button>去管理</el-button>
  </div>
</template>

<script>
export default {
  name: 'CoverPage'
  
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