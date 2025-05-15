<template>
  <transition name="fade">
    <el-alert
      :title="title"
      type="error"
      show-icon
      :closable="false"
      class="floating-alert"
    />
  </transition>
</template>

<script>
export default {
  props: ['title', ]
}
</script>

<style scoped>
.floating-alert {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  opacity: 0.9;
  width: auto;
  max-width: 80%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>