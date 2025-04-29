<template>
  <div id="app">
    <el-button>{{ greeting }}，lyq大王</el-button>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    greeting() {
      const hour = new Date().getHours()
      if (hour >= 5 && hour < 12) {
        return '早上好'
      } else if (hour >= 12 && hour < 18) {
        return '下午好'
      } else {
        return '晚上好'
      }
    }
  }
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
