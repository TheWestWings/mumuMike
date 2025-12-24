<template>
  <div class="revenue-container">
    <h2 class="page-title">营收统计</h2>
    
    <!-- 统计摘要卡片 -->
    <el-row :gutter="20" class="summary-cards">
      <el-col :xs="24" :sm="8">
        <el-card class="summary-card today" shadow="hover">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-coin"></i></div>
            <div class="card-info">
              <p class="card-label">今日营收</p>
              <p class="card-value">¥{{ todayRevenue.toFixed(2) }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="summary-card week" shadow="hover">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-data-line"></i></div>
            <div class="card-info">
              <p class="card-label">本周营收</p>
              <p class="card-value">¥{{ weekRevenue.toFixed(2) }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="summary-card orders" shadow="hover">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-s-order"></i></div>
            <div class="card-info">
              <p class="card-label">总订单数</p>
              <p class="card-value">{{ totalOrders }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="14">
        <el-card class="chart-card" shadow="hover">
          <div slot="header" class="chart-header">
            <span>每日营收趋势</span>
          </div>
          <div ref="revenueChart" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card class="chart-card" shadow="hover">
          <div slot="header" class="chart-header">
            <span>订单状态分布</span>
          </div>
          <div ref="orderStatusChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card" shadow="hover">
          <div slot="header" class="chart-header">
            <span>热门产品销量排行 TOP 8</span>
          </div>
          <div ref="productRankChart" class="chart-large"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getAllOrderList } from '@/api/Order/Order'

export default {
  name: 'RevenueMgmt',
  data() {
    return {
      todayRevenue: 0,
      weekRevenue: 0,
      totalOrders: 0,
      revenueChart: null,
      orderStatusChart: null,
      productRankChart: null,
      orderList: []
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.revenueChart) this.revenueChart.dispose()
    if (this.orderStatusChart) this.orderStatusChart.dispose()
    if (this.productRankChart) this.productRankChart.dispose()
  },
  methods: {
    async loadData() {
      try {
        const res = await getAllOrderList()
        this.orderList = res.data?.rows || res.rows || []
        this.calculateStatistics()
        this.$nextTick(() => {
          this.initCharts()
        })
      } catch (err) {
        console.error('加载数据失败', err)
      }
    },
    
    calculateStatistics() {
      const now = new Date()
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      const weekStart = new Date(today)
      weekStart.setDate(weekStart.getDate() - 7)

      let todayRev = 0
      let weekRev = 0

      this.orderList.forEach(order => {
        const orderDate = new Date(order.createTime)
        const orderTotal = this.calculateOrderTotal(order)
        
        if (orderDate >= today) {
          todayRev += orderTotal
        }
        if (orderDate >= weekStart) {
          weekRev += orderTotal
        }
      })

      this.todayRevenue = todayRev
      this.weekRevenue = weekRev
      this.totalOrders = this.orderList.length
    },

    calculateOrderTotal(order) {
      let total = 0
      ;(order.product || []).forEach(item => {
        if (item.product && item.status !== 2) {
          total += (item.product.price || 0) * (item.count || 0)
        }
      })
      return total
    },

    initCharts() {
      this.initRevenueChart()
      this.initOrderStatusChart()
      this.initProductRankChart()
    },

    initRevenueChart() {
      const chart = echarts.init(this.$refs.revenueChart)
      this.revenueChart = chart

      // 计算最近7天每日营收
      const days = []
      const revenues = []
      const now = new Date()
      
      for (let i = 6; i >= 0; i--) {
        const date = new Date(now)
        date.setDate(date.getDate() - i)
        const dateStr = `${date.getMonth() + 1}/${date.getDate()}`
        days.push(dateStr)
        
        const dayStart = new Date(date.getFullYear(), date.getMonth(), date.getDate())
        const dayEnd = new Date(dayStart)
        dayEnd.setDate(dayEnd.getDate() + 1)
        
        let dayRevenue = 0
        this.orderList.forEach(order => {
          const orderDate = new Date(order.createTime)
          if (orderDate >= dayStart && orderDate < dayEnd) {
            dayRevenue += this.calculateOrderTotal(order)
          }
        })
        revenues.push(dayRevenue)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: days, boundaryGap: false },
        yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
        series: [{
          name: '营收',
          type: 'line',
          smooth: true,
          data: revenues,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(141, 110, 99, 0.5)' },
              { offset: 1, color: 'rgba(141, 110, 99, 0.1)' }
            ])
          },
          lineStyle: { color: '#8d6e63', width: 3 },
          itemStyle: { color: '#8d6e63' }
        }]
      }
      chart.setOption(option)
    },

    initOrderStatusChart() {
      const chart = echarts.init(this.$refs.orderStatusChart)
      this.orderStatusChart = chart

      // 统计订单状态
      const statusCount = { 0: 0, 1: 0, 2: 0 }
      this.orderList.forEach(order => {
        if (statusCount[order.status] !== undefined) {
          statusCount[order.status]++
        }
      })

      const option = {
        tooltip: { trigger: 'item' },
        legend: { bottom: 10 },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          label: { show: true, formatter: '{b}: {c}' },
          data: [
            { value: statusCount[0], name: '制作中', itemStyle: { color: '#e6a23c' } },
            { value: statusCount[1], name: '待取餐', itemStyle: { color: '#409eff' } },
            { value: statusCount[2], name: '已完成', itemStyle: { color: '#67c23a' } }
          ]
        }]
      }
      chart.setOption(option)
    },

    initProductRankChart() {
      const chart = echarts.init(this.$refs.productRankChart)
      this.productRankChart = chart

      // 统计产品销量
      const productSales = {}
      this.orderList.forEach(order => {
        (order.product || []).forEach(item => {
          if (item.product && item.status !== 2) {
            const name = item.product.name || '未知产品'
            productSales[name] = (productSales[name] || 0) + (item.count || 0)
          }
        })
      })

      // 排序取前10
      const sortedProducts = Object.entries(productSales)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 10)
      
      const productNames = sortedProducts.map(p => p[0])
      const productCounts = sortedProducts.map(p => p[1])

      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: productNames.reverse(), axisLabel: { interval: 0 } },
        series: [{
          name: '销量',
          type: 'bar',
          data: productCounts.reverse(),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#a88e85' },
              { offset: 1, color: '#8d6e63' }
            ])
          },
          label: { show: true, position: 'right' }
        }]
      }
      chart.setOption(option)
    },

    handleResize() {
      if (this.revenueChart) this.revenueChart.resize()
      if (this.orderStatusChart) this.orderStatusChart.resize()
      if (this.productRankChart) this.productRankChart.resize()
    }
  }
}
</script>

<style scoped>
.revenue-container {
  padding: 10px;
}

.page-title {
  font-size: 24px;
  color: #5d4037;
  margin-bottom: 20px;
  font-weight: 600;
}

.summary-cards {
  margin-bottom: 20px;
}

.summary-card {
  border-radius: 12px;
  margin-bottom: 15px;
}

.summary-card.today {
  background: linear-gradient(135deg, #8d6e63, #a88e85);
}

.summary-card.week {
  background: linear-gradient(135deg, #5d4037, #795548);
}

.summary-card.orders {
  background: linear-gradient(135deg, #6d4c41, #8d6e63);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.card-icon {
  font-size: 40px;
  color: rgba(255, 255, 255, 0.9);
  margin-right: 20px;
}

.card-info {
  color: #fff;
}

.card-label {
  font-size: 14px;
  margin: 0 0 5px;
  opacity: 0.9;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 12px;
  margin-bottom: 15px;
}

.chart-header {
  font-size: 16px;
  font-weight: 600;
  color: #5d4037;
}

.chart {
  height: 300px;
}

.chart-large {
  height: 400px;
}

/deep/ .el-card__body {
  padding: 15px;
}

/deep/ .el-card__header {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}
</style>
