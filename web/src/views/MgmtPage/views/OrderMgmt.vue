<template>
  <div>
      <el-descriptions
         class="margin-top" 
         :column="4" 
         border
         v-for="(item, index) in receiveList"
         :key="index"
         :title="item.createTime"
         >
         <template slot="extra">
          <el-button @click="handleRelease(item)">已取餐</el-button>
         </template>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{ item.user.username }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        {{ item.user.phone }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          取餐码
        </template>
        {{ item.id }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          订单状态
        </template>
        <el-tag>{{ orderStatus[item.status] }}</el-tag>
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          产品
        </template>
        
          <el-table
            :data="item.product"
            style="width: 100%">
            <el-table-column
              label="产品名称"
              width="180">
              <template slot-scope="scope">
                <!-- <i class="el-icon-time"></i> -->
                <span style="margin-left: 10px">{{ scope.row.product.name }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="单价"
              width="180">
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.product.price }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="数量"
              width="180">
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.count }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="总价"
              width="180">
              <template slot-scope="scope">
                
                <span style="margin-left: 10px">{{ scope.row.product.price * scope.row.count }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="状态"
              width="180">
              <template slot-scope="scope">
                <el-tag>{{ produStatus[scope.row.status] }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="danger"
                  plain
                  @click="handleChargeback(scope.row)">退单</el-button>
                  
                <el-button
                  size="mini"
                  type="success"
                  plain
                  @click="handleFinish(scope.row)">完成</el-button>
              </template>
            </el-table-column>
          </el-table>

      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import { getOrderList, updateOrderProductStatus, updateOrderStatus } from '@/api/Order/Order'
export default {
  created() {
    this.getList()

  },

  data(){
    return {
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
    }
  },
  methods: {
    getList() {
      getOrderList().then(res => {
        this.receiveList = res.data.rows
        // this.receiveList.forEach((item, index) => {
        //   this.receiveList[index] = item.product.length 
        // })
        console.log(this.receiveList)
      })
    },
    handleChargeback(row) {
      row.status = 2
      updateOrderProductStatus({id: row.id, status: row.status}).then(() => {
        this.getList()
      })
    },
    handleFinish(row) {
      row.status = 1
      updateOrderProductStatus({id: row.id, status: row.status}).then(() => {
        this.getList()
      })
      
    },
    handleRelease(item) {
      console.log({id: item.id, status: 2})
      updateOrderStatus({id: item.id, status: 2}).then(() => {
        this.getList()
      })

    }
    
  }

}
</script>

<style>

</style>