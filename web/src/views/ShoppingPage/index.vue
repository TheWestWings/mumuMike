<template>
      <el-container>
        <el-button @click="drawer = true" type="primary">
          点我打开
        </el-button>

        <el-drawer
          title="个人中心"
          :visible.sync="drawer"
          :size="400"
          class="user-drawer"
        >

          <div class="demo-basic--circle">
            <div class="block">
              <el-avatar :size="100" :src="circleUrl"></el-avatar>
            </div>
          </div>

        <el-form
         label-position="top"
         :rules="editRule"
         :model="user"
         ref="userForm"
         >
          <el-form-item label="用户名" prop="username">
            <el-input
             :readonly="!onEdit"
             v-model="user.username"
             :class="{'input-edit': onEdit}"
             ></el-input>
          </el-form-item>

          <el-form-item label="电话" prop="phone">
            <el-input
             :readonly="!onEdit"
             v-model="user.phone"
             :class="{'input-edit': onEdit}"
             ></el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input
             :readonly="!onEdit"
             v-model="user.email"
             :class="{'input-edit': onEdit}"
             ></el-input>
          </el-form-item>

          <el-form-item size="large" v-if="onEdit">
            <el-button type="primary" @click="handleUpdateUserInfo">确认修改</el-button>
          </el-form-item>


          <el-form-item size="large" v-else>
            <el-button @click="handleOnEdit">修改信息</el-button>
          </el-form-item>


        </el-form>
        <el-button @click="handleLogout">退出登录</el-button>
        

          
        </el-drawer>

        <el-button @click="$router.push('/HomePage')">返回首页</el-button>


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
import { updateUserInfo } from '@/api/User/User'
import classForm from './components/classForm.vue'
import ProductCard from './components/productCard.vue'
import { getSeriesList } from '@/api/Shopping/Shopping'
export default {
  name: "ShoppingPage",
  components: { classForm, ProductCard },

  mounted(){
    this.getList()

    this.user.username = this.$store.state.username
    this.user.pswd = this.$store.state.pswd
    this.user.phone = this.$store.state.phone
    this.user.email = this.$store.state.email
  },

  methods: {
    getList() {
      getSeriesList().then(res => {
      console.log(res.data)
      this.seriesList = res.data.rows
      
    })
    },
    handleOnEdit() {
      this.onEdit = true
    }
    ,
    handleUpdateUserInfo() {

      updateUserInfo(this.user).then(() => {

        this.$store.commit('setUsername', this.user.username)
        this.$store.commit('setEmail', this.user.email)
        this.$store.commit('setPhone', this.user.phone)
        this.$message({
          message: "修改成功！",
          type: 'success'
        })
      })
      this.onEdit = false
    },

    handleLogout() {
      this.$store.commit('clearAll')
      this.$router.push('/')
    }

  },


  
  data(){
    return{

      user: {
        username: '',
        phone: '',
        email: '',
      },

      onEdit: false,

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

      ],

      editRule: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 3, max: 16, message: '长度在4到16个字符', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur'},
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phone: [
          { required: true, message: '请输入电话号码', trigger: 'blur'},
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }
        ],
      },
    }
  }



}
</script>

<style lang="less" scoped>
.user-drawer {
  .el-drawer__header {
    margin-bottom: 0;
    padding: 20px;
    font-size: 18px;
    font-weight: bold;
    border-bottom: 1px solid #eee;
  }

  .el-drawer__body {
    padding: 20px;
  }
}

.demo-basic--circle {
  display: flex;
  justify-content: center;
  margin: 30px 0;
  
  .block {
    text-align: center;

    .el-avatar {
      border: 3px solid #fff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.05);
      }
    }
  }
}

.el-form {
  padding: 0 20px;

  .el-form-item {
    margin-bottom: 25px;

    .el-input__inner {
      border-radius: 4px;
      background-color: #f5f7fa;
      transition: all 0.3s ease;
    }

    .input-edit {
      background-color: #ffffff !important;
      border: none !important;
      outline: 2px solid #8d6e63 !important;
      outline-offset: -1px !important;
      border-radius: 4px !important;
      box-shadow: 0 2px 8px rgba(141, 110, 99, 0.15);
      &:focus {
        background-color: #fff !important;
        box-shadow: 0 4px 12px rgba(141, 110, 99, 0.2);
      }
    }
  }
}

.el-button {
  width: calc(100% - 40px);
  margin: 10px 20px;
  border-radius: 4px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
}

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