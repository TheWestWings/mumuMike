<template>
  <div>
    <img :src="product.pictureUrl" style="height: 50px;">
    <span>{{ product.price }}￥</span>
    <el-input-number
      v-model="now.count" 
      @change="handleChange" 
      :min="1" 
      :max="10" 
      label="描述文字"
      step-strictly
      ></el-input-number>
  </div>


</template>

<script>
export default {
  props: ['product'],
  mounted() 
  { 
    
  }, 
  created(){
    console.log('传过来了',this.product)
    this.now = this.product
    this.now.count = this.product.count
    

  },
  watch: {
    product :function(val){
      this.now = val
      console.log('product更新了数量', this.now)
    }
  },
  data() {
    return {
      now: {
        count: 0,

      }
      

    }
  },
  methods: {
    handleChange(value) {
      this.$emit('countChange', this.now)
      console.log('现在', this.now.id, '的数量是',value);
    }
  }
  

}
</script>

<style>

</style>