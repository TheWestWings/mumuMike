<template>
   <div class="category-section" :id="id">
    <div class="category-header">
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
    </div>
    <div class="products">
        <slot name="products"></slot>
    </div>
</div>
</template>

<script>

export default {
    props: ['title', 'description', 'id'],
    data() {
        return{
            series: {
                title: '糖水系列',
                description: '传统与创新的完美融合，温暖你的味蕾'
            }
        }
    },
    mounted(){
        // 添加进入动画效果
        this.$nextTick(() => {
            setTimeout(() => {
                this.$el.classList.add('animate-in');
            }, 400 + Math.random() * 400); // 随机延迟，创造错落感
            
            // 为产品卡片添加交错动画
            const productCards = this.$el.querySelectorAll('.product-card');
            productCards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('animate-in');
                }, 600 + (index * 120)); // 交错显示产品卡片
            });
        });
    }
}
</script>

<style scoped>
/* 分类区域的进入动画 */
.category-section {
    opacity: 0;
    transform: translateY(40px);
    transition: all 0.8s cubic-bezier(0.215, 0.61, 0.355, 1);
}

.category-section.animate-in {
    opacity: 1;
    transform: translateY(0);
}

/* 产品卡片的交错动画由父组件添加的样式控制 */
.category-section {
    margin: 6rem 0;
}

.category-header {
    text-align: center;
    margin-bottom: 2rem;
    position: relative;
}

.category-header h2 {
    font-size: 2em;
    color: #4e342e;
    margin-bottom: 0.5rem;
}

.category-header p {
    color: #795548;
    font-size: 1.1em;
}

.category-header::after {
    content: '';
    display: block;
    width: 60px;
    height: 3px;
    background-color: #8d6e63;
    margin: 1rem auto;
}

.products {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 2fr));
    gap: 2rem;
    margin: 2rem 0;
}


</style>