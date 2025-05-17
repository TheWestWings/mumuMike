<template>
  <div>

    <el-collapse v-model="activeNames" @change="handleChange"
        v-for="(item, index) in orderList"
         :key="index">
      <el-collapse-item :title="'取餐码 '+item.number" name="index">
        
        <el-descriptions
         class="margin-top" 
         :column="4" 
         border
         >

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{ item.username }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        {{ item.phone }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          取餐码
        </template>
        {{ item.number }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          订单状态
        </template>
        <el-tag>{{ item.state }}</el-tag>
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          产品
        </template>
        
          <el-table
            :data="item.productsList"
            style="width: 100%">
            <el-table-column
              label="产品名称"
              width="180">
              <template slot-scope="scope">
                <!-- <i class="el-icon-time"></i> -->
                <span style="margin-left: 10px">{{ scope.row.name }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="数量"
              width="180">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>姓名: {{ scope.row.name }}</p>
                  <p>单价: {{ scope.row.price }}</p>
                  <div slot="reference" class="name-wrapper">
                    <span style="margin-left: 10px">{{ scope.row.count }}</span>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column
              label="总价"
              width="180">
              <template slot-scope="scope">
                
                <span style="margin-left: 10px">{{ scope.row.price * scope.row.count }}</span>
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
export default {
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
          state: '已完成',
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

        }

      ]
    }
}
}
</script>

<style>

</style>