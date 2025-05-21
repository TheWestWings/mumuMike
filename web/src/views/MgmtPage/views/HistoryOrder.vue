<template>
  <div class="history-order-container" v-loading="loading" element-loading-text="正在加载历史订单数据...">
    <h2 class="page-title">历史订单</h2>
    
    <div v-if="receiveList.length === 0" class="empty-state">
      <i class="el-icon-tickets empty-icon"></i>
      <p>暂无历史订单</p>
    </div>

    <el-collapse 
      class="order-collapse"
      v-for="(item, index) in receiveList"
      :key="index"
      v-model="activeNames">
      <el-collapse-item :name="index">
        <template slot="title">
          <div class="collapse-title">
            <span class="order-code">订单号: {{ item.id }}</span>
            <span class="order-time">{{ "创建时间: " + formatDate(item.createTime) }}</span>
            <span class="order-time">{{ "完成时间:  "+ formatDate(item.updateTime) }}</span>
            <el-tag 
              class="order-status-tag" 
              :type="getStatusType(item.status)" 
              size="small"
              effect="dark"
            >
              {{ orderStatus[item.status] }}
            </el-tag>
          </div>
        </template>
        
        <el-descriptions
          class="order-description" 
          :column="4" 
          border
          size="medium"
        >
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
              :type="getStatusType(item.status)" 
              effect="plain"
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
                min-width="120">
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
            </el-table>
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import { getHistoryOrderList } from '@/api/Order/Order'

export default {
  mounted() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getHistoryOrderList().then((res) => {
        this.receiveList = res.data.rows
        console.log(this.receiveList)
      }).finally(() => {
        this.loading = false
      })
    },
    handleChange(val) {
      console.log(val)
    },
    // 格式化日期为 YYYY-MM-DD HH:MM:SS 格式
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
    },
    getStatusType(status) {
      return this.statusTypeMap[status] || 'info'
    },
    getProductStatusType(status) {
      return this.productStatusTypeMap[status] || 'info'
    }
  },
  data() {
    return {
      loading: false,
      receiveList: [],
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
      },
      activeNames: []
    }
  }
}
</script>

<style scoped>
.history-order-container {
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

.order-collapse {
  margin-bottom: 16px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.order-collapse /deep/ .el-collapse-item__header {
  background-color: #f5f7fa;
  padding: 0 20px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  height: 50px;
  line-height: 50px;
}

.order-collapse /deep/ .el-collapse-item__content {
  padding: 15px;
}

.collapse-title {
  display: flex;
  align-items: center;
  width: 100%;
}

.order-code {
  font-weight: 600;
  color: #409EFF;
  margin-right: 15px;
}

.order-time {
  color: #606266;
  margin-right: 0px;
  font-size: 14px;
  flex-grow: 1;
}

.order-status-tag {
  margin-left: auto;
}

.order-description {
  margin: 0;
}

.order-description /deep/ .el-descriptions__header {
  display: none;
}

.order-description /deep/ .el-descriptions-item__label {
  background-color: #f5f7fa;
  padding: 12px 15px;
}

.order-description /deep/ .el-descriptions-item__content {
  padding: 12px 15px;
}

.info-text {
  font-weight: 500;
}

.pickup-code {
  font-weight: 600;
  color: #409EFF;
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
  min-height: 100px;
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
  margin-bottom: 20px;
}

/* 响应式布局调整 */
@media screen and (max-width: 768px) {
  .collapse-title {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .order-time {
    margin: 5px 0;
  }
  
  .order-status-tag {
    margin-left: 0;
  }
  
  .order-description /deep/ .el-descriptions-item {
    width: 100%;
  }
  
  .product-table /deep/ .el-table__body {
    width: 100% !important;
  }
}
</style>