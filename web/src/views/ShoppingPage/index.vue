<template>
      <el-container style="background-color: #FDF6E3;" >
        <div class="top-nav">
          <el-button @click="drawer.user = true" type="text" class="nav-btn">
            <i class="el-icon-user"></i> 个人中心
          </el-button>
          <el-button @click="drawer.message = true" type="text" class="nav-btn">
            <i class="el-icon-bell"></i> 消息中心
          </el-button>
          <el-button @click="$router.push('/HomePage')" type="text" class="nav-btn">
            <i class="el-icon-s-home"></i> 返回首页
          </el-button>
        </div>

        <el-drawer
          :visible.sync="drawer.message"
          title="消息中心"
          :size="400"
        >
          <div class="message-content">
            <!-- 消息内容区域 -->
          </div>
        </el-drawer>

        <el-drawer
          title="个人中心"
          :visible.sync="drawer.user"
          :size="400"
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


        <div class="side-nav" :class="{ 'mobile': isMobile, 'collapsed': isCollapsed }">
          <div class="nav-content">
            <el-button
              v-for="(series, index) in seriesList"
              :key="index"
              type="text"
              class="nav-menu-btn"
              @click="scrollTo(getSeriesId(index))"
            >
              <i :class="seriesIcons[index]"></i>
              <span class="nav-text">{{ series.title }}</span>
            </el-button>
          </div>
          <el-button
            class="collapse-btn"
            type="text"
            @click="toggleCollapse"
          >
            <i :class="isCollapsed ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'"></i>
          </el-button>
        </div>

        <el-header style="height: 450px;">
          <header class="header">
            <div class="header-content">
                <h1>木木牛奶公司</h1>
                <p>用心制作的奶香甜品</p>
            </div>
          </header>


        </el-header>


        <el-main class="main-content">
          <class-form
            v-for="(series, index) in seriesList"
            :key="index"
            :title="series.title"
            :description="series.description"
            :id="getSeriesId(index)"
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

        <div class="bottom-bar">
          <el-badge :value="totalCount" :hidden="totalCount === 0" class="cart-badge">
            <el-button type="primary" @click="drawer.car = true" class="cart-btn">
              <i class="el-icon-shopping-cart-2"></i>
              购物车
            </el-button>
          </el-badge>
          <div class="total-price">
            总价: ¥{{ calculateTotal() }}
          </div>
          <el-button type="success" @click="submitOrder" class="submit-btn">
            提交订单
          </el-button>
        </div>

        <el-drawer
          title="购物车"
          direction="btt"
          :visible.sync="drawer.car"
          :size="isMobile ? '70%' : '50%'"
          :style="{ width: drawerWidth, left: drawerLift }"
          :class="{'mobile-drawer': isMobile}"
        >
          <div class="cart-content">
            <product-selection
              @countChange="handleCountChange"
              v-for="(item, index) in carProductList"
              :key="index"
              :product="item"
              class="cart-item"
            ></product-selection>
          </div>
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
import {getMessageList} from '@/api/Message/Message'

export default {
  name: "ShoppingPage",
  components: { classForm, ProductCard, ProductSelection },

  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },

  mounted(){
    this.getList()
    
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize);
    
    // 为每个系列生成随机图标
    for(let i = 0; i < 10; i++) {  // 预先生成10个图标
      this.seriesIcons.push(this.getRandomIcon());
    }

    this.user.username = this.$store.state.username
    this.user.pswd = this.$store.state.pswd
    this.user.phone = this.$store.state.phone
    this.user.email = this.$store.state.email
    this.user.id = this.$store.state.id
    this.getMessage()
  },

  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed;
    },

    handleResize() {
      this.isMobile = window.innerWidth <= 768;
    },

    getRandomIcon() {
      const icons = [
        'el-icon-ice-cream-round',
        'el-icon-ice-cream-square',
        'el-icon-lollipop',
        'el-icon-potato-strips',
        'el-icon-milk-tea',
        'el-icon-ice-drink',
        'el-icon-ice-tea',
        'el-icon-coffee',
        'el-icon-orange',
        'el-icon-grape',
        'el-icon-watermelon',
        'el-icon-cherry'
      ];
      return icons[Math.floor(Math.random() * icons.length)];
    },

    getSeriesId(index) {
      const idMap = ['home', 'sugar', 'snow', 'milktea', 'plus'];
      return idMap[index] || `section-${index}`;
    },

    scrollTo(id) {
      const element = document.querySelector(`#${id}`);
      if (element) {
        element.scrollIntoView({
          behavior: 'smooth',
          block: 'start'
        });
      }
    },

    getTotalCount() {
      return this.carProductList.reduce((total, item) => {
        return total + item.count
      }, 0)
    },
    calculateTotal() {
      return this.carProductList.reduce((total, item) => {
        return total + (item.price * item.count)
      }, 0).toFixed(2)
    },

    getList() {
      getSeriesList().then(res => {
        this.seriesList = res.data.rows
      })
    },
    getMessage() {
      console.log( 'id',this.user.id)
      getMessageList(this.user.id).then(res => {
        console.log( 'message',res)
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
        this.totalCount = this.getTotalCount()
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


  
  computed: {
    totalCount() {
      return this.getTotalCount()
    }
  },
  watch: {
    isMobile(newVal) {
      if (newVal) {
        this.drawerWidth = '100%';
        this.drawerLift = '0%';
      }
    else {
        this.drawerWidth = '50%';
        this.drawerLift = '25%';
    }
    }
  },

  data(){
    return{
      isMobile: window.innerWidth <= 768,  // 添加移动端判断
      drawerWidth: '50%',
      drawerLift: '25%',
      isCollapsed: false,
      seriesIcons: [],  // 存储每个系列对应的图标

      user: {
        username: '',
        phone: '',
        email: '',
      },

      onEdit: false,

      drawer: {
        user: false,
        car: false,
        message: false
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

<style lang="less">
.mobile-drawer {
  .el-drawer__container {
    .el-drawer {
      width: 100% !important;
      left: 50% !important;
      transform: translateX(-50%) !important;
      max-width: 100%;

      .el-drawer__body {
        padding: 20px;
        
        .cart-content {
          width: 92%;
          margin: 0 auto;
        }
      }

      .el-drawer__header {
        padding: 15px;
        margin-bottom: 0;
        text-align: center;
      }
    }
  }
}

/* 基础样式保持不变 */

/* 移动端样式 */
@media screen and (max-width: 768px) {
  .top-nav {
    width: 100%;
    top: 0;
    left: 0;
    transform: none;
    border-radius: 0;
    background: rgba(141, 110, 99, 0.98);

    .nav-btn {
      padding: 8px 12px;
      margin-left: 5px;
      font-size: 12px;

      i {
        margin-right: 4px;
        font-size: 14px;
      }
    }
  }

  .side-nav {
    bottom: 10px;
    top: auto;
    left: 50%;
    transform: translateX(-50%);
    width: 92%;
    border-radius: 30px;
    background-color: rgba(141, 110, 99, 0.95);
    padding: 10px 0;
    height: 360px;
    max-height: 360px;
    
    .nav-content {
      flex-direction: row;
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(90px, 1fr));
      gap: 18px;
      padding: 15px 15px 15px 12px;
      justify-items: start;
      justify-content: start;
      height: auto;
      max-height: 320px;
      overflow-y: auto;
      -webkit-overflow-scrolling: touch;
      
      &::-webkit-scrollbar {
        display: none;
      }
    }

    .nav-menu-btn {
      padding: 10px 5px;
      height: 85px;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 12px;
      background: rgba(255, 255, 255, 0.15);
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      i {
        margin: 0 0 10px 0;
        font-size: 28px;
        color: #FFE4C4;
      }

      .nav-text {
        font-size: 13px;
        color: #FFE4C4;
        font-weight: 500;
        line-height: 1.2;
        text-align: center;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      &:hover, &:active {
        background-color: rgba(255, 255, 255, 0.25);
      }
    }

    .collapse-btn {
      display: none;
    }
  }

  .main-content {
    padding: 60px 10px 120px 10px !important;
  }

  .bottom-bar {
    width: 92% !important;
    border-radius: 30px !important;
    bottom: 20px !important;
    

    .cart-btn, .submit-btn {
      padding: 8px 12px;
      font-size: 14px;
    }

    .total-price {
      font-size: 14px;
      margin: 0 10px;
    }
  }

  .header {
    height: 300px !important;
    margin-top: 50px;

    h1 {
      font-size: 24px !important;
    }

    p {
      font-size: 14px !important;
    }
  }

  .el-drawer {
    width: 90% !important;
  }
}

/* 平板样式 */
@media screen and (min-width: 769px) and (max-width: 1024px) {
  .top-nav {
    width: 90%;
  }

  .side-nav {
    width: 50px;
    
    .nav-content {
      padding: 10px 0;
    }

    .nav-menu-btn {
      padding: 15px 0;
      justify-content: center;

      i {
        margin: 0;
        font-size: 20px;
      }

      .nav-text {
        display: none;
      }
    }

    &:hover {
      width: 180px;

      .nav-menu-btn {
        padding: 12px 25px;
        justify-content: flex-start;

        i {
          margin-right: 10px;
        }

        .nav-text {
          display: block;
        }
      }
    }
  }

  .main-content {
    padding-left: 70px !important;
  }

  .bottom-bar {
    width: 70% !important;
  }
}

.top-nav {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1001;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 25px;
  background: rgba(141, 110, 99, 0.95);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  border-radius: 25px;
  white-space: nowrap;

  .nav-btn {
    color: #fff;
    font-size: 14px;
    padding: 8px 20px;
    margin-left: 15px;
    border-radius: 20px;
    transition: all 0.3s;

    &:hover {
      color: #ffd04b;
      background-color: rgba(255, 255, 255, 0.1);
      transform: translateY(-2px);
    }

    i {
      margin-right: 6px;
      font-size: 16px;
    }
  }
}

.side-nav {
  position: fixed;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(141, 110, 99, 0.95);
  border-radius: 0 15px 15px 0;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  transition: all 0.3s ease;
  width: 220px;
  max-height: 80vh;

  &.mobile.collapsed {
    transform: translate(-100%, -50%);
    
    .collapse-btn {
      transform: translate(100%, -50%) rotate(180deg);
      right: -50px;
    }
  }

  &:not(.mobile).collapsed {
    width: 70px;

    .nav-text {
      opacity: 0;
      width: 0;
      display: none;
    }

    .nav-menu-btn {
      padding: 15px 0;
      width: 70px;

      i {
        margin: 0;
        width: 70px;
        font-size: 22px;
      }
    }
  }

  &:not(.mobile) {
    .nav-content {
      display: flex;
      flex-direction: column;
      padding: 25px 0;
      gap: 8px;
    }

    .nav-menu-btn {
      color: #fff;
      font-size: 15px;
      padding: 15px 30px;
      text-align: left;
      position: relative;
      transition: all 0.3s;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      margin: 0;
      border-radius: 0;

      i {
        font-size: 20px;
        margin-right: 15px;
        flex-shrink: 0;
        width: 24px;
        text-align: center;
        transition: all 0.3s ease;
      }

      .nav-text {
        transition: opacity 0.3s ease, width 0.3s ease;
        white-space: nowrap;
        overflow: hidden;
        flex: 1;
      }

      &:hover {
        color: #ffd04b;
        background-color: rgba(255, 255, 255, 0.1);
        padding-left: 15px;
      }
    }
  }

  .collapse-btn {
    position: absolute;
    right: -15px;
    top: 50%;
    transform: translateY(-50%);
    width: 30px;
    height: 30px;
    padding: 0;
    border-radius: 50%;
    background: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #8d6e63;

    &:hover {
      background: #ffd04b;
      color: #fff;
    }

    i {
      font-size: 16px;
    }
  }
}


.bottom-bar {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 50%;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  z-index: 1000;
  border-radius: 30px;

  .cart-badge {
    margin: 0;
  }

  .cart-btn, .submit-btn {
    width: 120px;
    margin: 0;
    border-radius: 20px;
    font-size: 16px;
  }


  

  .total-price {
    flex: 1;
    text-align: center;
    font-size: 18px;
    font-weight: bold;
    color: #8d6e63;
    margin: 0 20px;
  }

  .submit-btn {
    width: 120px;
    margin: 0;

  }
}



.shopping-cart-drawer {
  &.el-drawer--bottom {
    height: 500px !important;
    width: 50% !important;
    left: 25% !important;
  }
}

.cart-content {
  padding: 20px;
  max-height: calc(80vh - 60px);
  overflow-y: auto;

  .cart-item {
    margin-bottom: 15px;
    padding: 15px;
    background-color: #f8f8f8;
    border-radius: 8px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
  }
}

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