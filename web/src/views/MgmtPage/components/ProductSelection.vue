<template>
  <div class="product-selection">
    <img :src="product.pictureUrl" class="product-image">
    <span class="product-price">￥{{ product.price }}</span>
    <span class="product-name">{{ product.name }}</span>
    <el-input-number
      v-model="now.count" 
      @change="handleChange" 
      :min="0" 
      :max="10" 
      label="描述文字"
      step-strictly
      class="quantity-input"
    />
  </div>
</template>

<script>
export default {
  props: ['product'],
  mounted() { 
  }, 
  created(){
    console.log('传过来了',this.product)
    this.now = this.product
    this.now.count = this.product.count
  },
  watch: {
    product: {
        handler(newVal) {
            this.now = newVal
        },
        deep: true
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

<style scoped>
.product-selection {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.product-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.product-price {
  font-size: 16px;
  font-weight: 600;
  color: #ff6b6b;
  min-width: 80px;
}

.quantity-input {
  margin-left: auto;
}

/* 覆盖 element-ui 输入框样式 */
.quantity-input :deep(.el-input-number__decrease),
.quantity-input :deep(.el-input-number__increase) {
  background-color: #f5f7fa;
  border-color: #dcdfe6;
}

.quantity-input :deep(.el-input__inner) {
  text-align: center;
}
</style>