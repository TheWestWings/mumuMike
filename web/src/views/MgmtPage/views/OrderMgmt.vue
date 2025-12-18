<template>
  <div class="order-mgmt-container" v-loading="loading" element-loading-text="正在加载订单数据...">
    <h2 class="page-title">订单管理</h2>
    <div
      v-for="(item, index) in receiveList"
      :key="index"
      class="order-card"
    >
      <el-descriptions
        :column="4"
        border
        size="medium"
        :title="formatDate(item.createTime)"
        class="order-description"
      >
        <template slot="extra">
          <el-button
            @click="handleBatchComplete(item)"
            size="medium"
            type="success"
            :disabled="item.status === 2"
            class="batch-complete-btn"
          >
            <i class="el-icon-finished"></i> 一键完成
          </el-button>
          <el-button
            @click="handleRelease(item)"
            size="medium"
            :disabled="item.status === 2"
            class="pickup-btn"
          >
            <i class="el-icon-check"></i> 已取餐
          </el-button>
        </template>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <span class="info-text">{{ item.user.username }}</span>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          <span class="info-text">{{ item.user.phone }}</span>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            取餐码
          </template>
          <span class="pickup-code">{{ item.id }}</span>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-order"></i>
            订单状态
          </template>
          <el-tag
          plain

          >
            {{ orderStatus[item.status] }}
          </el-tag>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-shopping-cart-full"></i>
            产品
          </template>
          
          <el-table
            :data="item.product"
            class="product-table"
            stripe
            size="medium">
            <el-table-column
              label="产品名称"
              prop="product.name"
              min-width="120"
              align="center">
              <template slot-scope="scope">
                <span class="product-name">{{ scope.row.product.name }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="单价"
              min-width="80"
              align="center">
              <template slot-scope="scope">
                <span class="price-text">¥{{ scope.row.product.price }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="数量"
              min-width="80"
              align="center">
              <template slot-scope="scope">
                <span class="count-text">x{{ scope.row.count }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="总价"
              min-width="80"
              align="center">
              <template slot-scope="scope">
                <span class="total-price">¥{{ (scope.row.product.price * scope.row.count).toFixed(2) }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="状态"
              min-width="100"
              align="center">
              <template slot-scope="scope">
                <el-tag
                  :type="getProductStatusType(scope.row.status)"
                  size="mini"
                  effect="plain"
                >
                  {{ produStatus[scope.row.status] }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
              label="操作"
              min-width="150"
              align="center">
              <template slot-scope="scope">
                <el-button
                  size="medium"
                    type="danger" plain
                  :disabled="scope.row.status === 2 || scope.row.status === 1"
                  @click="handleChargeback(scope.row, item.id)">
                  退单
                </el-button>
                  
                <el-button
                  size="medium"
                    type="success" plain
                  :disabled="scope.row.status === 2 || scope.row.status === 1"
                  @click="handleFinish(scope.row, item.id)">
                 完成
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-descriptions-item>
      </el-descriptions>
    </div>
    
    <div v-if="receiveList.length === 0" class="empty-state">
      <i class="el-icon-tickets empty-icon"></i>
      <p>暂无订单</p>
    </div>
  </div>
</template>

<script>
import { getOrderList, updateOrderProductStatus, updateOrderStatus } from '@/api/Order/Order'
export default {
  created() {
    this.getList(true)
    // 设置定时器，每5秒自动刷新订单列表
    this.timer = setInterval(() => {
      this.getList(false) // 不显示加载状态，避免页面闪动
    }, 1000)
  },
  
  beforeDestroy() {
    // 组件销毁前清除定时器，防止内存泄漏
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  },

  data(){
    return {
      loading: false,
      timer: null, // 用于存储定时器ID
      orderList:[
        {
          username:'weec',
          phone: '15987536941',
          number: '152',
          state: '制作中',
          productsList: [
            {
              name: '珍珠奶茶',
              count: 1,
              price: 15,
              
            },
            {
              name: '珍珠奶茶',
              count: 2,
              price: 15,
               
            },
            {
              name: '珍珠奶茶',
              count: 3,
              price: 15,
            }
          ]

        },
        {
          username:'weec',
          phone: '15987536941',
          number: '152',
          state: '制作中',
          productsList: [
            {
              name: '珍珠奶茶',
              count: 1,
              price: 15,
              
            },
            {
              name: '珍珠奶茶',
              count: 2,
              price: 15,
               
            },
            {
              name: '珍珠奶茶',
              count: 3,
              price: 15,
            }
          ]

        },
        {
          username:'weec',
          phone: '15987536941',
          number: '152',
          state: '制作中',
          productsList: [
            {
              name: '珍珠奶茶',
              count: 1,
              price: 15,
              state: '制作中'
              
            },
            {
              name: '珍珠奶茶',
              count: 2,
              price: 15,
              state: '制作中'
               
            },
            {
              name: '珍珠奶茶',
              count: 3,
              price: 15,
              state: '制作中'
            }
          ]

        }

      ],
      receiveList:[],
      produStatus: ['待制作', '已完成', '已退单'],
      orderStatus: ['制作中', '待取餐', '已完成'],
      statusTypeMap: {
        0: 'warning',  // 制作中
        1: 'success',  // 待取餐
        2: 'info'      // 已完成
      },
      productStatusTypeMap: {
        0: 'warning',  // 待制作
        1: 'success',  // 已完成
        2: 'danger'    // 已退单
      }
    }
  },
  methods: {
    getList(showLoading = true) {
      if (showLoading) {
        this.loading = true
      }
      getOrderList().then(res => {
        this.receiveList = res.data.rows
        // this.receiveList.forEach((item, index) => {
        //   this.receiveList[index] = item.product.length
        // })
        console.log(this.receiveList)
      }).finally(() => {
        if (showLoading) {
          this.loading = false
        }
      })
    },
    handleChargeback(row, orderId) {
      // 弹出对话框，让用户输入退单原因
      this.$prompt('请输入退单原因', '退单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputValue: '产品材料不足',
        inputErrorMessage: '退单原因不能为空'
      }).then(({ value }) => {
        // 用户点击确定，将状态和退单原因一起发送到后端
        row.status = 2
        updateOrderProductStatus({
          id: row.id,
          status: row.status,
          comment: value  // 退单原因
        }).then(() => {
          this.getList(true)
          this.checkAndUpdateOrderStatus(orderId)
          this.$message({
            type: 'success',
            message: '退单成功'
          })
        })
      }).catch(() => {
        // 用户点击取消
        this.$message({
          type: 'info',
          message: '已取消退单'
        })
      })
    },
    // 格式化日期为 YYYY-MM-DD HH:MM:SS 格式
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
    },

    handleFinish(row, orderId) {
      row.status = 1
      updateOrderProductStatus({id: row.id, status: row.status}).then(() => {
        this.getList(true)
        this.$message({
          message: '产品制作完成',
          type: 'success'
        })
        this.checkAndUpdateOrderStatus(orderId)
      })
    },
    
    // 检查订单中所有产品状态并更新订单状态
    checkAndUpdateOrderStatus(orderId) {
      // 获取当前订单
      // 因为getList是异步的，这里需要等待一小段时间确保数据已经刷新
      setTimeout(() => {
        const currentOrder = this.receiveList.find(order => order.id === orderId)
        if (!currentOrder) return
      
      // 检查订单中是否所有产品都不是待制作状态(0)
      const allProductsProcessed = currentOrder.product.every(product => product.status !== 0)
      
      // 如果所有产品都不是待制作状态，且当前订单仍处于"制作中"状态(0)
      if (allProductsProcessed && currentOrder.status === 0) {
        // 更新订单状态为"待取餐"(1)
        updateOrderStatus({id: orderId, status: 1}).then(() => {
          this.getList(true)
          this.$message({
            message: '订单已更新为待取餐状态',
            type: 'success'
          })
        })
      }
      }, 300) // 300毫秒的延迟，确保getList已经完成
    },
    
    handleRelease(item) {
      console.log({id: item.id, status: 2})
      
      // 检查订单中是否存在待制作(status=0)的产品
      const hasPendingProducts = item.product.some(product => product.status === 0)
      
      if (hasPendingProducts) {
        // 如果存在待制作的产品，显示警告消息并阻止状态更新
        this.$message({
          message: '订单中仍有待制作的产品，无法完成订单',
          type: 'warning'
        })
        return
      }
      
      // 只有当所有产品都不是待制作状态时，才允许更新订单状态
      updateOrderStatus({id: item.id, status: 2}).then(() => {
        this.getList(true)
        this.$message({
          message: '订单已完成',
          type: 'success'
        })
      })
    },
    
    // 一键完成：完成所有产品并标记订单为已完成
    async handleBatchComplete(item) {
      // 获取所有未完成的产品
      const pendingProducts = item.product.filter(p => p.status === 0)
      
      if (pendingProducts.length === 0) {
        // 无待制作产品，直接标记订单完成
        updateOrderStatus({id: item.id, status: 2}).then(() => {
          this.getList(true)
          this.$message({
            message: '订单已完成',
            type: 'success'
          })
        })
        return
      }
      
      try {
        // 批量完成所有待制作产品
        const promises = pendingProducts.map(product => 
          updateOrderProductStatus({id: product.id, status: 1})
        )
        await Promise.all(promises)
        
        // 更新订单状态为已完成
        await updateOrderStatus({id: item.id, status: 2})
        
        this.getList(true)
        this.$message({
          message: '订单一键完成',
          type: 'success'
        })
      } catch (err) {
        this.$message({
          message: '操作失败，请重试',
          type: 'error'
        })
      }
    },
    
    getStatusType(status) {
      return this.statusTypeMap[status] || 'info'
    },
    
    getProductStatusType(status) {
      return this.productStatusTypeMap[status] || 'info'
    }
  }

}
</script>

<style scoped>
.order-mgmt-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
  border-left: 4px solid #409EFF;
  padding-left: 12px;
}

.order-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s;
}

.order-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.2);
}

.order-description {
  margin: 0;
}

.order-description /deep/ .el-descriptions__header {
  padding: 20px 20px;
  margin: 0;
  background-color: #f5f7fa;
}

.order-description /deep/ .el-descriptions__title {
  font-weight: 600;
  color: #8d6e63;
}

.order-description /deep/ .el-descriptions-item__label {
  background-color: #f5f7fa;
  padding: 12px 15px;
}

.order-description /deep/ .el-descriptions-item__content {
  padding: 12px 15px;
}

.pickup-btn {
  margin-right: 10px;
}

.info-text {
  font-weight: 500;
}

.pickup-code {
  font-weight: 600;
  color: #8d6e63;
  font-size: 16px;
}

.product-table {
  width: 100%;
  margin-top: 5px;
}

.product-table /deep/ .el-table__header-wrapper th {
  background-color: #f5f7fa;
  color: #606266;
}

.product-table /deep/ .el-table__body-wrapper {
  min-height: 50px;
}

.product-name {
  font-weight: 500;
  color: #303133;
}

.price-text {
  color: #606266;
}

.count-text {
  font-weight: 500;
  color: #606266;
}

.total-price {
  font-weight: 600;
  color: #f56c6c;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #909399;
}

.empty-icon {
  font-size: 60px;
  /* margin-bottom: 20px; */
}

/* 响应式布局调整 */
@media screen and (max-width: 768px) {
  .order-description /deep/ .el-descriptions-item {
    width: 100%;
  }
  
  .product-table /deep/ .el-table__body {
    width: 100% !important;
  }
}
</style>