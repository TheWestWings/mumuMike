<template>
   <div class="category-section" id="sugar">
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
    props: ['title', 'description'],
    data() {

        return{
            series: {
                title: '糖水系列',
                description: '传统与创新的完美融合，温暖你的味蕾'
            }

        }
    },
    mounted(){
        console.log(this.title)
        console.log(this.description)
    }
}
</script>

<style scoped>
.category-section {
    margin: 4rem 0;
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
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 2rem;
    margin: 2rem 0;
}


</style>