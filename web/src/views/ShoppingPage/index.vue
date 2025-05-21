<template>
      <el-container style="background-color: #FDF6E3;" >
        <div class="top-nav">
            <el-button @click="$router.push('/HomePage')" type="text" class="nav-btn">
                <i class="el-icon-s-home"></i> 返回首页
            </el-button>
          <el-button @click="drawer.user = true" type="text" class="nav-btn">
            <i class="el-icon-user"></i> 个人中心
          </el-button>
          <el-badge :value="unreadMessageCount" :hidden="unreadMessageCount === 0" class="message-badge">
            <el-button @click="drawer.message = true" type="text" class="nav-btn">
              <i class="el-icon-bell"></i> 消息中心
            </el-button>
          </el-badge>

        </div>

        <el-drawer
          :visible.sync="drawer.message"
          title="消息中心"
          class="message-drawer"
          :size="550"
          :with-header="false"
        >
          <div class="drawer-header">
            <div class="drawer-title">消息中心</div>
            <el-button class="drawer-close" icon="el-icon-close" circle @click="drawer.user = false"></el-button>
          </div>
          <div class="message-content">
            <!-- 消息内容区域 -->
            <div v-for="(item, index) in messageList" :key="item.id">
              <!-- 时间分界线 -->
              <div v-if="index === 0 || formatDate(item.createTime) !== formatDate(messageList[index-1].createTime)" class="message-time-divider">
                <span>{{ formatDate(item.createTime) }}</span>
              </div>
              
              <!-- 消息卡片 -->
              <div class="message-item">
                <el-card :class="{'unread': item.status === 0}" @click.native="toggleMessage(item)">
                  <div class="message-header">
                    <div class="message-title-row">
                      <span class="message-title">{{ item.title }}</span>
                      <span v-if="item.status === 0" class="unread-dot"></span>
                    </div>
                  </div>
                  <div v-if="item.expanded || item.showContent" class="message-body">
                    {{ item.content }}
                  </div>
                </el-card>
              </div>
            </div>
          </div>
        </el-drawer>

        <el-drawer
          title="个人中心"
          :visible.sync="drawer.user"
          :size="550"
          class="user-drawer"
          :with-header="false"
        >
          <div class="drawer-header">
            <div class="drawer-title">个人中心</div>
            <el-button class="drawer-close" icon="el-icon-close" circle @click="drawer.user = false"></el-button>
          </div>
          <div class="demo-basic--circle">
            <div class="block">
              <el-upload
                class="avatar-uploader"
                action="http://localhost:8080/updateAvatar"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :headers="{
                    Authorization: 'Bearer ' + $store.state.token
            }"
            
                >
                <el-avatar
                  :size="100"
                  :src="user.avatar ? user.avatar : ''"
                  @error="() => false">
                  <i v-if="!user.avatar" class="el-icon-user-solid"></i>
                </el-avatar>
                <div class="avatar-hover-text">点击上传</div>
              </el-upload>
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
        <el-form-item size="large" v-if="onEdit" class="center-btn-item">
        <el-button type="primary" @click="handleUpdateUserInfo" class="center-btn">确认修改</el-button>
        </el-form-item>


<el-form-item size="large" v-else class="center-btn-item">
  <el-button @click="handleOnEdit" class="center-btn">修改信息</el-button>
</el-form-item>



        </el-form>
        <div class="drawer-footer">
          <el-button @click="handleLogout" type="danger" icon="el-icon-switch-button">退出登录</el-button>
        </div>
          
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

        <el-header style="height: auto; padding: 0;">
          <header class="header">
            <div class="header-backdrop"></div>
            <div class="header-content">
                <h1>木木牛奶公司</h1>
                <p>用心制作的奶香甜品</p>
                <div class="header-decoration">
                  <span></span><span></span><span></span>
                </div>
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
              v-for="(product, productIndex) in series.productList"
                :key="productIndex"
                :product="product"
                @click="handleAdd"
                ref="productCards"
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
          :class="{'mobile-drawer': isMobile, 'cart-drawer': true}"
        >
          <div class="cart-content">
            <div v-if="carProductList.length === 0" class="empty-cart">
              <i class="el-icon-shopping-cart-2"></i>
              <p>购物车是空的，快去选购吧~</p>
            </div>
            <product-selection
              @countChange="handleCountChange"
              v-for="(item, index) in carProductList"
              :key="index"
              :product="item"
              class="cart-item"
            ></product-selection>
          </div>
          <div class="cart-footer">
            <div class="cart-footer-item">
              <span class="cart-summary">共 {{ totalCount }} 件商品</span>
            </div>
            <div class="cart-footer-item">
              <span class="cart-total-label">合计:</span>
              <span class="cart-total-price">¥{{ calculateTotal() }}</span>
            </div>
            <el-button
              type="primary"
              @click="submitOrder"
              class="cart-checkout-btn"
              :disabled="carProductList.length === 0"
            >
              去结算
            </el-button>
          </div>
        </el-drawer>

        </el-main> 
        <el-footer>Footer</el-footer>
        
        <!-- 右侧留言按钮 -->
        <div class="comment-btn-container">
          <el-button
            @click="drawer.comment = true"
            type="primary"
            class="comment-btn"
            icon="el-icon-s-comment"
            circle>
          </el-button>
        </div>
        
        <!-- 留言抽屉 -->
        <el-drawer
          title="留言板"
          :visible.sync="drawer.comment"
          :size="isMobile ? '100%' : '550px'"
          :direction="isMobile ? 'btt' : 'rtl'"
          class="comment-drawer"
        >
          <div class="drawer-header">
            <div class="drawer-title">留言板</div>
            <el-button class="drawer-close" icon="el-icon-close" circle @click="drawer.comment = false"></el-button>
          </div>
          
          <div class="comment-content">
            <el-form
              :model="commentForm"
              :rules="commentRules"
              ref="commentForm"
              label-position="top"
              class="comment-form"
            >
              <el-form-item label="标题" prop="title">
                <el-input
                  v-model="commentForm.title"
                  placeholder="请输入留言标题"
                  maxlength="30"
                  show-word-limit
                ></el-input>
              </el-form-item>
              
              <el-form-item label="内容" prop="content">
                <el-input
                  type="textarea"
                  v-model="commentForm.content"
                  placeholder="请输入留言内容"
                  :rows="5"
                  maxlength="200"
                  show-word-limit
                ></el-input>
              </el-form-item>
              
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitComment"
                  class="submit-comment-btn"
                  :loading="submitting"
                >
                  提交留言
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-drawer>
    </el-container>

</template>

<script>
import { updateUserInfo, getInfo } from '@/api/User/User'
import classForm from './components/classForm.vue'
import ProductCard from './components/productCard.vue'
import { getSeriesList } from '@/api/Shopping/Shopping'
import ProductSelection from '../MgmtPage/components/ProductSelection.vue'
import { getProductById } from '@/api/Product/Product'
import { submitOrder } from '@/api/Order/Order'
import {getMessageList, updateMessageStatus} from '@/api/Message/Message'
import { addInformation } from '@/api/Information/Information'

export default {
  name: "ShoppingPage",
  components: { classForm, ProductCard, ProductSelection },

  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },
  created(){
    this.carProductList = this.$store.state.car.carList ? this.$store.state.car.carList : []
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
    this.user.avatar = this.$store.state.avatar

    this.drawerWidth = this.isMobile ? '100%' : '50%';  // 设置抽屉宽度
    this.drawerLift = this.isMobile ? '0%' : '25%';  // 设置抽屉位置
    this.carProductList = this.$store.state.car.carList ? this.$store.state.car.carList : []
    console.log('购物车', this.$store.state.car.carList)
    this.getMessage()
    
    // 移除所有动画效果，直接显示页面元素
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
        this.messageList = res.data.rows.map(item => {
          return {
            ...item,
            expanded: false,
            showContent: false
          }
        })
      })
    },

    // 格式化日期为 YYYY-MM-DD HH:MM:SS 格式
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
    },

    toggleMessage(item) {
      // 切换消息展开/收起状态
      item.expanded = !item.expanded
      
      // 如果是未读消息，标记为已读
      if (item.status === 0) {
        item.status = 1
        updateMessageStatus({id: item.id, status: item.status}).then(() => {
        })
   
      }
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
        this.$notify({
          title: "成功",
          message: "修改成功！",
          type: 'success'
        })
      })
      this.onEdit = false
    },
    
    // 头像上传成功处理
    handleAvatarSuccess(res, file) {
      if (res.code === 200) {
        this.user.avatar = res.data.url
        this.$store.commit('setAvatar', this.user.avatar)
        this.$notify({
          title: "成功",
          message: `头像"${file.name}"上传成功！`,
          type: 'success'
        })
        // 调用更新用户信息的接口，保存头像URL
        getInfo().then((res) => {
            this.$store.commit('setAvatar', res.data.data.avatar)
            this.user.avatar = res.data.data.avatar
          console.log('头像更新成功')
        })
      } else {
        this.$notify.error({
          title: "错误",
          message: '头像上传失败'
        })
      }
    },
    
    // 头像上传前的校验
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$notify.error({
          title: "格式错误",
          message: '上传头像图片只能是 JPG/PNG/GIF 格式!'
        })
      }
      if (!isLt2M) {
        this.$notify.error({
          title: "大小错误",
          message: '上传头像图片大小不能超过 2MB!'
        })
      }
      return isJPG && isLt2M
    },

    handleLogout() {
      this.$store.commit('clearAll')
        this.$store.commit('car/clearCarList')
      this.$router.push('/')
    },

    ///////////购物车
    handleCountChange(now) {
      this.carProductList.forEach((item, index) => {
        console.log(now.productId)

        if(item.productId === now.productId){
          if(now.count === 0) {
            // 删除当前物品
            this.carProductList.splice(index, 1)
          }
          else {
            this.carProductList[index].count = now.count
            console.log('我要加了', item.count)
          }
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
      // 检查购物车是否为空
      if (this.carProductList.length === 0) {
        this.$notify({
          title: "提示",
          message: "购物车是空的，请先添加商品",
          type: 'warning'
        });
        return;
      }
      
      this.order.createTime = new Date()
      this.order.product = this.carProductList
      console.log(this.order)
      
      // 显示加载中
      const loading = this.$loading({
        lock: true,
        text: '订单提交中...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.7)'
      });
      
      submitOrder(this.order).then((res) => {
        console.log(res)
        
        // 关闭加载中
        loading.close();
        
        // 显示成功提示
        this.$notify({
          title: "成功",
          message: "订单提交成功！",
          type: 'success'
        });
        
        // 关闭购物车抽屉
        this.drawer.car = false;
        
        // 清空购物车
        this.carProductList = [];
        
        // 更新总数量
        this.totalCount = 0;
      }).catch(error => {
        // 关闭加载中
        loading.close();
        
        // 显示错误提示
        this.$notify({
          title: "错误",
          message: "订单提交失败，请稍后重试",
          type: 'error'
        });
        
        console.error("提交订单失败:", error);
      });
    },
    
    // 提交留言
    submitComment() {
      this.$refs.commentForm.validate((valid) => {
        if (valid) {
          this.submitting = true;
          
          // 这里需要调用后端API提交留言
          // 假设API路径为 /message/add
          this.$notify({
            title: "提示",
            message: "留言提交中...",
            type: 'info',
            duration: 2000
          });
          
          addInformation(this.commentForm).then(() => {
            // 模拟API调用，实际项目中应该替换为真实的API调用
            // 例如: submitComment(this.commentForm).then(...)
            this.submitting = false;
            this.$notify({
              title: "成功",
              message: "留言提交成功！",
              type: 'success'
            });
            
            // 清空表单
            this.commentForm.title = '';
            this.commentForm.content = '';
            this.$refs.commentForm.resetFields();
            
            // 关闭抽屉
            this.drawer.comment = false;
          });
        } else {
          this.$notify({
            title: "错误",
            message: "请正确填写留言信息",
            type: 'error'
          });
          return false;
        }
      });
    }


  },
  
  computed: {
    totalCount() {
      return this.getTotalCount()
    },
    unreadMessageCount() {
      return this.messageList.filter(item => item.status === 0).length;
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
    },
    carProductList: {
        handler(newVal) {
            console.log('购物车更新了数量', newVal)
            this.$store.commit('car/setCarList', newVal)
        },
        deep: true
    }
  },

  data(){
    return{
      isMobile: window.innerWidth <= 768,  // 添加移动端判断
      drawerWidth: this.isMobile ? '100%' : '50%',  // 设置抽屉宽度
      drawerLift: this.isMobile ? '0%' : '25%',  // 设置抽屉位置
      isCollapsed: false,
      seriesIcons: [],  // 存储每个系列对应的图标
      submitting: false, // 提交留言状态

      user: {
        username: '',
        phone: '',
        email: '',
        avatar: '',
      },

      onEdit: false,

      drawer: {
        user: false,
        car: false,
        message: false,
        comment: false // 新增留言抽屉
      },
      
      // 留言表单数据
      commentForm: {
        title: '',
        content: '',
        userId: this.$store.state.id
      },
      
      // 留言表单验证规则
      commentRules: {
        title: [
          { required: true, message: '请输入留言标题', trigger: 'blur' },
          { min: 2, max: 30, message: '标题长度在2到30个字符之间', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入留言内容', trigger: 'blur' },
          { min: 5, max: 200, message: '内容长度在5到200个字符之间', trigger: 'blur' }
        ]
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
      messageList:[]
    }
  }



}
</script>

<style lang="less">
/* 已移除所有动画效果 */

/* 移动端抽屉样式 */
.mobile-drawer {
  .el-drawer__container {
    .el-drawer {
      width: 100% !important;
      left: 0 !important;
      transform: none !important;
      max-width: 100%;
      border-radius: 20px 20px 0 0 !important;

      .el-drawer__body {
        padding: 0;
        
        .cart-content {
          width: 100%;
          padding: 15px;
          max-height: 60vh;
        }
      }

      .el-drawer__header {
        padding: 12px 15px;
        margin-bottom: 0;
        text-align: center;
        font-size: 16px;
        font-weight: 600;
        border-radius: 20px 20px 0 0;
        background-color: #f8f6f2;
        border-bottom: 1px solid #eee;
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
    //   justify-items: start;
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
      margin: 0;
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
    width: 100% !important;
    border-radius: 0 !important;
    bottom: 0 !important;
    left: 0 !important;
    transform: none !important;
    display: flex !important;
    opacity: 1 !important;
    visibility: visible !important;
    z-index: 2000 !important;
    position: fixed !important;
    
    .cart-btn, .submit-btn {
      padding: 8px 12px;
      font-size: 14px;
      width: auto !important;
    }

    .total-price {
      font-size: 14px;
      margin: 0 10px;
    }
  }

  .header {
    height: 300px !important;
    margin-top: 50px;
    border-radius: 0 0 20px 20px;

    h1 {
      font-size: 28px !important;
    }

    p {
      font-size: 14px !important;
      margin-bottom: 20px;
    }
    
    .header-decoration {
      margin-top: 20px;
      
      span {
        height: 3px;
      }
      
      span:nth-child(1) {
        width: 20px;
      }
      
      span:nth-child(2) {
        width: 40px;
      }
      
      span:nth-child(3) {
        width: 20px;
      }
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

  .header {
    height: 350px !important;
    border-radius: 0 0 25px 25px;
    
    h1 {
      font-size: 3em !important;
    }
    
    .header-decoration {
      margin-top: 25px;
    }
  }

  .side-nav {
    width: 180px;
    
    .nav-content {
      padding: 10px 0;
    }

    .nav-menu-btn {
      padding: 12px 15px;
      justify-content: flex-start;

      i {
        margin-right: 10px;
        font-size: 20px;
      }

      .nav-text {
        font-size: 14px;
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
  opacity: 1; /* 始终保持可见 */


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
  position: fixed !important;
  bottom: 20px !important;
  left: 50% !important;
  transform: translateX(-50%) !important;
  width: 50% ;
  height: 60px !important;
  background-color: #fff !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
  display: flex !important;
  justify-content: space-between !important;
  align-items: center !important;
  padding: 0 20px !important;
  z-index: 2000 !important;
  border-radius: 30px !important;
  opacity: 1 !important;
  visibility: visible !important;

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



/* 购物车抽屉样式 */
.cart-drawer {
  &.el-drawer--bottom {
    height: auto !important;
    min-height: 50vh;
    max-height: 70vh;
    width: 50% !important;
    left: 25% !important;
    border-radius: 20px 20px 0 0 !important;
    overflow: hidden;
  }
  
  .el-drawer__wrapper {
    border-radius: 20px 20px 0 0;
    overflow: hidden;
  }
  
  .el-drawer__container {
    border-radius: 20px 20px 0 0;
    overflow: hidden;
  }
  
  .el-drawer {
    border-radius: 20px 20px 0 0 !important;
    overflow: hidden;
  }
  
  .el-drawer__header {
    margin-bottom: 0;
    padding: 16px 20px;
    background-color: #f8f6f2;
    border-bottom: 1px solid #eee;
    font-size: 18px;
    font-weight: 600;
    color: #8d6e63;
    border-radius: 20px 20px 0 0;
    
    .el-drawer__close-btn {
      color: #8d6e63;
      
      &:hover {
        color: #5d4037;
      }
    }
  }
  
  .el-drawer__body {
    padding: 0;
    position: relative;
    background-color: #fdf6e3;
    display: flex;
    flex-direction: column;
  }
}

.cart-content {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
  background: linear-gradient(to bottom, rgba(255,255,255,0.8), rgba(255,255,255,0.2));
  -webkit-overflow-scrolling: touch;

  .empty-cart {
    text-align: center;
    padding: 40px 20px;
    color: #aaa;
    
    i {
      font-size: 50px;
      color: #ddd;
      margin-bottom: 15px;
    }
    
    p {
      font-size: 15px;
    }
  }

  .cart-item {
    margin-bottom: 15px;
    padding: 15px;
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 12px;
    transition: all 0.3s;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    border: 1px solid rgba(141, 110, 99, 0.1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(141, 110, 99, 0.15);
    }
  }
}

/* 购物车底部样式 */
.cart-footer {
  background-color: #fff;
  border-top: 1px solid #eee;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 -4px 10px rgba(0, 0, 0, 0.05);
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 10;
  
  .cart-footer-item {
    display: flex;
    align-items: center;
    
    .cart-summary {
      font-size: 14px;
      color: #909399;
    }
    
    .cart-total-label {
      font-size: 15px;
      color: #606266;
      margin-right: 8px;
    }
    
    .cart-total-price {
      font-size: 20px;
      font-weight: 600;
      color: #8d6e63;
    }
  }
  
  .cart-checkout-btn {
    padding: 10px 20px;
    border-radius: 20px;
    font-size: 16px;
    background-color: #8d6e63;
    border-color: #8d6e63;
    transition: all 0.3s;
    
    &:hover {
      background-color: #5d4037;
      border-color: #5d4037;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(93, 64, 55, 0.25);
    }
  }
}

/* 适配移动端购物车 */
@media screen and (max-width: 768px) {
  .cart-drawer {
    &.el-drawer--bottom {
      width: 100% !important;
      left: 0 !important;
      border-radius: 20px 20px 0 0 !important;
      z-index: 2001 !important;
      height: auto !important;
      min-height: 55vh !important;
      max-height: 80vh !important;
    }
    
    .el-drawer__wrapper,
    .el-drawer__container,
    .el-drawer,
    .el-drawer__header {
      border-radius: 20px 20px 0 0 !important;
    }
    
    .el-drawer__body {
      .cart-content {
        max-height: calc(75vh - 120px);
        padding: 15px;
      }
    }
  }
  
  .cart-footer {
    padding: 12px 15px;
    border-top: 1px solid rgba(141, 110, 99, 0.1);
    background-color: #f8f6f2;
    
    .cart-footer-item {
      &:first-child {
        display: none;
      }
      
      .cart-total-label {
        font-size: 14px;
      }
      
      .cart-total-price {
        font-size: 18px;
      }
    }
    
    .cart-checkout-btn {
      padding: 8px 15px;
      font-size: 14px;
      border-radius: 16px;
      width: 120px;
      font-weight: 500;
      background-color: #8d6e63;
      border-color: #8d6e63;
      
      &:hover, &:active {
        background-color: #5d4037;
        border-color: #5d4037;
      }
    }
  }
  
  .cart-item {
    margin-bottom: 10px;
    
    &:last-child {
      margin-bottom: 5px;
    }
  }
}

.main-content {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 1rem;
}
.user-drawer {
  .el-drawer {
    border-radius: 16px 0 0 16px !important;
    overflow: hidden;
  }
  
  .drawer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background-color: #ffffff;
    border-bottom: 1px solid #eee;
    
    .drawer-title {
      font-size: 20px;
      font-weight: bold;
      color: #8d6e63;
    }
    
    .drawer-close {
      padding: 8px;
      color: #8d6e63;
      border: none;
      
      &:hover {
        background-color: rgba(141, 110, 99, 0.1);
        color: #5d4037;
      }
    }
  }

  .el-drawer__body {
    padding: 24px;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
  }
  
  .drawer-footer {
    padding: 20px 0;
    margin-top: 20px;
    background-color: transparent;
  }
}

/* 适配移动端个人中心抽屉 */
@media screen and (max-width: 768px) {
  .user-drawer {
    .el-drawer__header {
      padding: 15px;
      font-size: 16px;
    }
    
    .demo-basic--circle {
      margin: 20px 0;
    }
    
    .el-form {
      padding: 0 15px;
    }
    
    .drawer-footer {
      padding: 10px 0;
    }
  }
}

.message-drawer {
      .el-drawer {
    border-radius: 16px 0 0 16px !important;
    overflow: hidden;
  }
  
  .drawer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background-color: #ffffff;
    border-bottom: 1px solid #eee;
    
    .drawer-title {
      font-size: 20px;
      font-weight: bold;
      color: #8d6e63;
    }
    
    .drawer-close {
      padding: 8px;
      color: #8d6e63;
      border: none;
      
      &:hover {
        background-color: rgba(141, 110, 99, 0.1);
        color: #5d4037;
      }
    }
  }

  .el-drawer__body {
    padding: 24px;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
  }
  
  .drawer-footer {
    padding: 20px 0;
    margin-top: 20px;
    background-color: transparent;
  }
}

.demo-basic--circle {
  display: flex;
  justify-content: center;
  margin: 30px 0;
  position: relative;
  
  &:after {
    content: '';
    position: absolute;
    bottom: -15px;
    left: 50%;
    transform: translateX(-50%);
    width: 60px;
    height: 3px;
    background-color: #8d6e63;
    border-radius: 2px;
    opacity: 0.5;
  }
  
  .block {
    text-align: center;

    .avatar-uploader {
      position: relative;
      display: inline-block;
      cursor: pointer;

      .el-avatar {
        border: 4px solid #fff;
        box-shadow: 0 4px 15px rgba(141, 110, 99, 0.2);
        transition: all 0.3s ease;
        background-color: #f8f6f2;
      }

      &:hover {
        .el-avatar {
          transform: scale(1.05);
          box-shadow: 0 6px 20px rgba(141, 110, 99, 0.3);
          opacity: 0.9;
        }
        
        .avatar-hover-text {
          opacity: 1;
          transform: translate(-50%, -50%) scale(1);
        }
      }
      
      .avatar-hover-text {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%) scale(0.9);
        color: #fff;
        font-size: 14px;
        font-weight: bold;
        opacity: 0;
        transition: all 0.3s ease;
        text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
        padding: 5px 10px;
        background-color: rgba(141, 110, 99, 0.7);
        border-radius: 12px;
        white-space: nowrap;
        pointer-events: none;
      }
    }
  }
}

.user-drawer .el-form {
  padding: 30px;
  margin: 15px 0 25px 0;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(0);
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 30px rgba(141, 110, 99, 0.12);
  }
  
  .el-form-item__label {
    color: #8d6e63;
    font-weight: 600;
    font-size: 16px;
    padding-bottom: 8px;
    letter-spacing: 0.5px;
  }

  .el-form-item {
    margin-bottom: 30px;
    position: relative;
    
    &:last-child {
      margin-bottom: 15px;
    }

    .el-input__inner {
      border-radius: 12px;
      height: 48px;
      padding-left: 16px;
      background-color: #f9f9f9;
      transition: all 0.3s ease;
      border-color: #e8e8e8;
      font-size: 15px;
    }

    .input-edit {
      background-color: #ffffff !important;
      border: 2px solid #8d6e63 !important;
      border-radius: 12px !important;
      box-shadow: 0 4px 12px rgba(141, 110, 99, 0.12);
      
      &:focus {
        background-color: #fff !important;
        box-shadow: 0 6px 16px rgba(141, 110, 99, 0.18);
      }
    }
  }
  
  .center-btn-item {
    display: flex;
    justify-content: center;
    margin-top: 10px;
  }
  
  .center-btn {
    width: 200px !important;
    height: 42px;
    font-size: 15px;
    letter-spacing: 1px;
    border-radius: 8px;
    transition: all 0.3s;
    margin: 0 auto;
    
    &.el-button--primary {
      background-color: #8d6e63;
      border-color: #8d6e63;
      
      &:hover, &:focus {
        background-color: #5d4037;
        border-color: #5d4037;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(93, 64, 55, 0.25);
      }
    }
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(141, 110, 99, 0.15);
    }
  }
}

.user-drawer .drawer-footer .el-button {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  display: block;
  border-radius: 12px;
  transition: all 0.3s;
  height: 48px;
  font-size: 16px;
  letter-spacing: 1px;
  
  &.el-button--danger {
    background-color: #f56c6c;
    border-color: #f56c6c;
    color: white;
    box-shadow: 0 4px 12px rgba(245, 108, 108, 0.2);
    
    &:hover {
      background-color: #e64242;
      border-color: #e64242;
      transform: translateY(-3px);
      box-shadow: 0 8px 20px rgba(230, 66, 66, 0.3);
    }
  }
}

.header {
    height: 400px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #8d6e63;
    background-image: linear-gradient(135deg, #8d6e63 0%, #5d4037 100%);
    overflow: hidden;
    border-radius: 0 0 30px 30px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.header-backdrop {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("data:image/svg+xml,%3Csvg width='20' height='20' viewBox='0 0 20 20' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='%23ffffff' fill-opacity='0.05' fill-rule='evenodd'%3E%3Ccircle cx='3' cy='3' r='3'/%3E%3Ccircle cx='13' cy='13' r='3'/%3E%3C/g%3E%3C/svg%3E");
    opacity: 0.8;
    z-index: 1;
}

.header-content {
    text-align: center;
    color: #fff;
    position: relative;
    z-index: 2;
    padding: 40px 20px;
    max-width: 800px;
}

.header h1 {
    font-size: 3.5em;
    margin-bottom: 20px;
    text-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
    font-weight: 600;
    letter-spacing: 2px;
    color: #FFF;
}

.header p {
    font-size: 1.25em;
    margin-bottom: 30px;
    color: rgba(255, 255, 255, 0.9);
    text-shadow: 1px 2px 4px rgba(0, 0, 0, 0.2);
    letter-spacing: 1px;
}

.header-decoration {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 15px;
    margin-top: 30px;
}

.header-decoration span {
    display: block;
    height: 4px;
    background-color: #FDF6E3;
    border-radius: 2px;
}

.header-decoration span:nth-child(1) {
    width: 30px;
}

.header-decoration span:nth-child(2) {
    width: 60px;
}

.header-decoration span:nth-child(3) {
    width: 30px;
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

/* 消息中心样式 */
.message-content {
  padding: 15px;
  max-height: calc(100vh - 120px);
  overflow-y: auto;
}

.message-item {
  margin-bottom: 15px;
}

.message-item .el-card {
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
}

.message-item .el-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message-item .el-card.unread {
  border-left: 3px solid #8d6e63;
  background-color: #f0f9ff;
}

.message-header {
  padding-bottom: 10px;
  cursor: pointer;
}

.message-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.message-title {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.unread .message-title {
  color: #8d6e63;
}

.message-body {
  padding-top: 10px;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  border-top: 1px solid #eee;
  margin-top: 8px;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background-color: #F56C6C;
  border-radius: 50%;
  display: inline-block;
}

/* 时间分界线样式 */
.message-time-divider {
  position: relative;
  text-align: center;
  margin: 20px 0;
  height: 16px;
  font-size: 14px;
}

.message-time-divider:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background-color: #EBEEF5;
  z-index: 1;
}

.message-time-divider span {
  position: relative;
  display: inline-block;
  padding: 0 10px;
  background-color: white;
  color: #909399;
  z-index: 2;
}

/* 留言按钮样式 */
.comment-btn-container {
  position: fixed;
  right: 20px;
  bottom: 400px;
  z-index: 999;
}

.comment-btn {
  width: 60px;
  height: 60px;
  box-shadow: 0 4px 12px rgba(141, 110, 99, 0.3);
  background-color: #8d6e63;
  border-color: #8d6e63;
  transition: all 0.3s;
}

.comment-btn:hover, .comment-btn:focus {
  background-color: #5d4037;
  border-color: #5d4037;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(93, 64, 55, 0.4);
}

.comment-btn i {
  font-size: 22px;
}

/* 留言抽屉样式 */
.comment-drawer {
  .el-drawer {
    border-radius: 16px 0 0 16px;
    overflow: hidden;
  }
  
  .drawer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background-color: #ffffff;
    border-bottom: 1px solid #eee;
    
    .drawer-title {
      font-size: 20px;
      font-weight: bold;
      color: #8d6e63;
    }
    
    .drawer-close {
      padding: 8px;
      color: #8d6e63;
      border: none;
      
      &:hover {
        background-color: rgba(141, 110, 99, 0.1);
        color: #5d4037;
      }
    }
  }
  
  .comment-content {
    padding: 20px;
    height: calc(100% - 70px);
    overflow-y: auto;
  }
  
  .comment-form {
    background-color: #fff;
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
    
    .el-form-item__label {
      color: #8d6e63;
      font-weight: 600;
      font-size: 16px;
      padding-bottom: 8px;
    }
    
    .el-input__inner, .el-textarea__inner {
      border-radius: 8px;
      border-color: #e0e0e0;
      transition: all 0.3s;
      
      &:focus {
        border-color: #8d6e63;
        box-shadow: 0 0 0 2px rgba(141, 110, 99, 0.2);
      }
    }
    
    .el-textarea__inner {
      min-height: 120px;
    }
    
    .submit-comment-btn {
      width: 100%;
      height: 44px;
      border-radius: 8px;
      background-color: #8d6e63;
      border-color: #8d6e63;
      font-size: 16px;
      font-weight: 500;
      letter-spacing: 1px;
      margin-top: 20px;
      transition: all 0.3s;
      
      &:hover, &:focus {
        background-color: #5d4037;
        border-color: #5d4037;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(93, 64, 55, 0.25);
      }
    }
  }
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .comment-btn-container {
    right: 15px;
    bottom: 100px;
  }
  
  .comment-btn {
    width: 50px;
    height: 50px;
  }
  
  .comment-drawer {
    .el-drawer {
      border-radius: 16px 16px 0 0 !important;
    }
    
    .drawer-header {
      padding: 15px;
    }
    
    .comment-content {
      padding: 15px;
    }
    
    .comment-form {
      padding: 15px;
    }
  }
}
</style>