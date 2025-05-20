<template>
      <el-container style="background-color: #FDF6E3;" >
        <el-button @click="drawer.user = true" type="primary">
          点我打开
        </el-button>

        <el-drawer
          title="个人中心"
          :visible.sync="drawer.user"
          :size="400"
          class="user-drawer"
        >

          <div class="demo-basic--circle">
            <div class="block">
              <el-avatar :size="100"></el-avatar>
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


        <el-main class="main-content">
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
                :product="product"
                @click="handleAdd"
              ></product-card>
              
            </template>
        </class-form>

        <el-button @click="drawer.car = true">购物车</el-button>

        <el-drawer
          direction="btt"
          :visible.sync="drawer.car"
          :size="400">
      
          <product-selection
            @countChange="handleCountChange"
            v-for="(item, index) in carProductList"
            :key="index"
            :product="item"
          ></product-selection>

          <el-button @click="submitOrder">提交订单</el-button>
        </el-drawer>

        </el-main> 
        <el-footer>Footer</el-footer>
    </el-container>

</template>

<script>
import { updateUserInfo } from '@/api/User/User'
import classForm from './components/classForm.vue'
import ProductCard from './components/productCard.vue'
import { getSeriesList } from '@/api/Shopping/Shopping'
import ProductSelection from '../MgmtPage/components/ProductSelection.vue'
import { getProductById } from '@/api/Product/Product'
import { submitOrder } from '@/api/Order/Order'

export default {
  name: "ShoppingPage",
  components: { classForm, ProductCard, ProductSelection },

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
    },

    ///////////购物车
    handleCountChange(now) {
      this.carProductList.forEach((item, index) => {
        console.log('我要加了', item.count)
        console.log(now.productId)

        if(item.productId === now.productId){
          this.carProductList[index].count = now.count
          console.log('我要加了', item.count)
        }
        
      })
    },
    handleAdd(id) {/////////产品上的+按钮
      let flag = false
      this.carProductList.forEach((item, index) => {
        if(item.productId === id) {
          console.log('yes')
          this.carProductList[index].count = item.count + 1
          flag = true
        }
      })
      console.log('list', this.carProductList)
      if(flag) return 0
      getProductById(id).then((res) => {
        let now = res.data.data 
        now.productId = now.id
        now.count = 1
        this.carProductList.push(now)
      })
    },
    submitOrder() {
      this.order.createTime = new Date()
      this.order.product = this.carProductList
      console.log(this.order)
      submitOrder(this.order).then((res) => {
        
        console.log(res)
      })
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

      drawer: {
        user: false,
        car: false
      },

      order:{
        userId: this.$store.state.id,
        product: [],
        createTime: null,
      },

      seriesList: [

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

      carProductList:[],
    }
  }



}
</script>

<style lang="less" scoped>

.main-content {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 1rem;
}
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