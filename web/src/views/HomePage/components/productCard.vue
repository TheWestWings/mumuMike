<template>
<div class="product-card">
    <img :src="pictureURL" alt="桃胶莲子鲜奶" class="product-image">
    <div class="product-info">
        <h3>{{ name }}</h3>
        <p>{{ description }}</p>
        <div class="product-price">¥{{ price }}</div>
    </div>
</div>
</template>

<script>
export default {
    props: ['name', 'description', 'price', 'pictureURL'],
    data() {
        return {
            product: {
                name: '桃胶莲子鲜奶',
                description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
                price: '18',
                pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
            }

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