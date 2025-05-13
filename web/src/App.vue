<template>
      <el-container>
        <el-header style="height: 450px;">
          <header class="header" >
            <div class="header-content">
                <h1>木木牛奶公司</h1>
                <p>用心制作的奶香甜品</p>
            </div>
          </header>

          <nav class="nav">
              <ul>
                  <li><a href="#home">首页</a></li>
                  <li><a href="#sugar">糖水系列</a></li>
                  <li><a href="#snow">白雪系列</a></li>
                  <li><a href="#milktea">鲜乳茶系列</a></li>
                  <li><a href="#plus">软糯小料</a></li>
              </ul>
          </nav>

        </el-header>
            <el-main>
              <class-form>
                <template v-slot:products>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                    <product-card></product-card>
                </template>
              </class-form>
            </el-main> 
        <el-footer>Footer</el-footer>
    </el-container>

</template>

<script>
import classForm from './views/HomePage/components/classForm.vue'
import ProductCard from './views/HomePage/components/productCard.vue'
export default {
  components: { classForm, ProductCard },

}
</script>

<style scope>

.header {
    height: 400px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(93, 64, 55, 0.8);
}

.header-content {
    text-align: center;
    color: #fff;
    position: relative;
    z-index: 2;
}

.header h1 {
    font-size: 3em;
    margin-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}


.nav {
    background-color: #8d6e63;
    padding: 1rem 0;
    position: sticky;
    top: 0;
    z-index: 100;
    height: 30px;
}
.nav ul {
    display: flex;
    justify-content: center;
    list-style: none;
    gap: 2rem;
}

.nav a {
    color: #fff;
    text-decoration: none;
    font-size: 1.1em;
    transition: color 0.3s;
}

.nav a:hover {
    color: #ffccbc;
}



</style>