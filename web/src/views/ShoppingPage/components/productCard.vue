<template>
<div class="product-card">
    <img :src="product.pictureUrl" alt="桃胶莲子鲜奶" class="product-image">
    <div class="product-info">
        <h3>{{ product.name }}</h3>
        <p>{{ product.description }}</p>
        <div class="product-price">¥{{ product.price }}</div>
    </div>
    <el-badge :hidden="!product.count" :value="product.count" class="item">
        <el-button
        circle
        @click="handleAdd"
        ><i class="el-icon-plus"></i></el-button>
     </el-badge>
</div>
</template>

<script>
export default {
    props: ['product'],
    watch: {
        product: function(val) {
            if(val.count && val. count > 0) this.isShow = true
        }

    },
    methods: {
        handleAdd() {
            this.$emit('click', this.product.id)
            console.log("click",  this.product.id)
        }

    },
    data() {
        return {
            isShow: false


        }
    }

}
</script>

<style scoped>
.product-card {
    background: #fff;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s;
}

.product-card:hover {
    transform: translateY(-5px);
}

.product-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
}

.product-info {
    padding: 1.5rem;
}

.product-info h3 {
    margin-bottom: 0.5rem;
    color: #4e342e;
}

.product-info p {
    color: #795548;
    line-height: 1.6;
}

.product-price {
    font-weight: bold;
    color: #d84315;
    margin-top: 0.5rem;
}

</style>