<template>
<div class="product-card">
    <img :src="product.pictureUrl" :alt="product.name" class="product-image">
    <div class="product-info">
        <h3>{{ product.name }}</h3>
        <p>{{ product.description }}</p>
        <div class="product-price">¥{{ product.price }}</div>
    </div>
    <el-badge :hidden="!count" :value="count" class="item">
        <el-button
        class="add-button"
        circle
        @click="handleAdd"
        ><i class="el-icon-plus"></i></el-button>
     </el-badge>
</div>
</template>

<script>
import { mapState } from 'vuex'

export default {
    props: ['product'],
    watch: {
        product: {
            handler(val) {
                if(val.count && val. count > 0) this.isShow = true
            },
            deep: true,
            immediate: true
        },
        carList: {
            handler(newVal) {
                let lst = newVal.filter(item => item.id === this.product.id)
                if(lst.length === 0) {
                    this.count = 0
                    return
                }
                else {
                    this.count = newVal.filter(item => item.id === this.product.id)[0].count
                }
                
            },
            deep: true,
            immediate: true
        }

    },
    methods: {
        handleAdd() {
            this.count++
            this.$emit('click', this.product.id)
            console.log("click",  this.product.id)
        }

    },
    data() {
        return {
            isShow: false,
            count: 0
        }
    },
    computed: {
        ...mapState('car',['carList'])
    },

}
</script>

<style scoped>
/* 产品卡片进入动画 */
.product-card {
    opacity: 0;
    transform: translateY(20px) scale(0.96);
    transition: all 0.5s cubic-bezier(0.215, 0.61, 0.355, 1);
}

.product-card.animate-in {
    opacity: 1;
    transform: translateY(0) scale(1);
}
.product-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    position: relative;
    padding-bottom: 1rem;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.12);
}

.product-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.product-card:hover .product-image {
    transform: scale(1.05);
}

.product-info {
    padding: 1.25rem 1.5rem;
}

.product-info h3 {
    margin: 0 0 0.75rem;
    color: #2c3e50;
    font-size: 1.1rem;
    font-weight: 600;
}

.product-info p {
    color: #5d6d7e;
    line-height: 1.5;
    font-size: 0.9rem;
    margin: 0 0 1rem;
    height: 2.8em;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

.product-price {
    font-weight: 600;
    color: #e74c3c;
    font-size: 1.2rem;
    margin-top: 0.5rem;
    display: inline-block;
    background: rgba(231, 76, 60, 0.1);
    padding: 0.3rem 0.8rem;
    border-radius: 20px;
}

.item {
    position: absolute;
    bottom: 1rem;
    right: 1rem;
}

.add-button {
    background-color: #a88e85;
    color: white;
    border: none;
    transition: all 0.3s ease;
}

.add-button:hover {
    background-color: #8d6e63;
    transform: scale(1.1);
}

.add-button:focus {
    outline: none;
}

.el-badge__content {
    background-color: #e74c3c;
}
</style>