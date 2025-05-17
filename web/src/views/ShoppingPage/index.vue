<template>
      <el-container>
        <el-button @click="drawer = true" type="primary">
          点我打开
        </el-button>

        <el-drawer
          title="个人中心"
          :visible.sync="drawer"
          
        >
        
          <el-col :span="12">
            <div class="sub-title"></div>
            <div class="demo-basic--circle">
              <div class="block"><el-avatar :size="50" :src="circleUrl"></el-avatar></div>
              <div class="block" v-for="size in sizeList" :key="size">
                <el-avatar :size="size" :src="circleUrl"></el-avatar>
              </div>
            </div>
          </el-col>  


          
        </el-drawer>


        <el-header style="height: 450px;">
          <header class="header" >
            <div class="header-content">
                <h1>木木牛奶公司</h1>
                <p>用心制作的奶香甜品</p>
            </div>
          </header>

          <el-menu
            class="nav1"
            mode="horizontal"

            background-color="#8d6e63"
            text-color="#fff"
            active-text-color="#ffd04b">
            <el-menu-item index="1"><a href="#home">首页</a></el-menu-item>
            <el-menu-item index="2"><a href="#sugar">糖水系列</a></el-menu-item>
            <el-menu-item index="3"><a href="#snow">白雪系列</a></el-menu-item>
            <el-menu-item index="4"><a href="#milktea">鲜乳茶系列</a></el-menu-item>
            <el-menu-item index="5"><a href="#plus">软糯小料</a></el-menu-item>
          </el-menu>

        </el-header>
            <el-main>
              <class-form
                v-for="(series, index) in seriesList"
                :key="index"
                :title="series.title"
                :description="series.description"
              >
                <template v-slot:products>
                  <product-card
                  v-for="(product, index) in series.productList"
                    :key="index"
                    :name="product.name"
                    :description="product.description"
                    :price="product.price"
                    :pictureURL="product.pictureUrl"
                  ></product-card>
                  <el-button></el-button>
                </template>
            </class-form>
            </el-main> 
        <el-footer>Footer</el-footer>
    </el-container>

</template>

<script>
import classForm from './components/classForm.vue'
import ProductCard from './components/productCard.vue'
import { getSeriesList } from '@/api/Shopping/Shopping'
export default {
  name: "ShoppingPage",
  components: { classForm, ProductCard },
  methods: {

  },
  mounted(){
    getSeriesList().then(res => {
      console.log(res.data)
      this.seriesList = res.data.rows
      
    })
  },


  
  data(){
    return{

      drawer: false,



      seriesList: [{
          title: '糖水系列',
          description: '传统与创新的完美融合，温暖你的味蕾',
          productList: [{
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          ]
      },
      {
          title: '糖水系列',
          description: '传统与创新的完美融合，温暖你的味蕾',
          productList: [{
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          ]
      },
      {
          title: '糖水系列',
          description: '传统与创新的完美融合，温暖你的味蕾',
          productList: [{
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          {
            name: '桃胶莲子鲜奶',
            description: '精选红豆熬制，搭配浓醇牛奶，香甜绵密。',
            price: '18',
            pictureURL: require('@/assets/products/taojiaolianzixiannai.png'),
          },
          ]
      },

      ]
    }
  }



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

a {
  text-decoration: none;
}

.nav1 {
  display: flex !important;
  justify-content: center !important;
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
}


</style>