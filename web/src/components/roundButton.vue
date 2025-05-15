<template>
  <el-row>  
  <el-button @click="f" type="primary" plain>{{ buttonType }}</el-button>
</el-row>
</template>

<script>

export default {
    props: ['buttonType'],

    mounted() {
      
    },
    methods: {
      f() {
        this.$emit('click')
      }

  },
}
</script>

<style>

</style>