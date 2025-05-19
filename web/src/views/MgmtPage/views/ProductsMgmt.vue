<template>
  <div>



    <el-table
      :data="ProductList"
      style="width: calc(100% - 40px); margin: 20px; border-radius: 8px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"
      :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight: 'bold', padding: '12px 0', textAlign: 'center'}"
      :row-style="{height: '60px'}"
      :cell-style="{padding: '0 15px', textAlign: 'center'}"
      >
    <el-table-column
      prop="name"
      label="产品名称"
      ></el-table-column>

    <el-table-column
      prop="description"
      label="详细"
      ></el-table-column>

    <el-table-column
      prop="price"
      label="价格/￥"
      ></el-table-column>


    <el-table-column
      prop="status"
      label="状态"
      >
      <template slot-scope="scope" >
        <span slot="reference" v-if="scope.row.status == 1">
          <!-- <i class="dotClass" style="background-color: #7D665F"></i> -->
          <i class="el-icon-success" style="color: #67C23A;"></i>
        </span>
        <span slot="reference" v-if="scope.row.status == 0" >
          
          <!-- <i class="dotClass" style="background-color: grey"></i> -->
          <i class="el-icon-error" style="color: #F56C6C;"></i>
        </span>
      </template>

    </el-table-column>

    <el-table-column
      label="状态设置"
      prop="status"
      >
      
      <template slot-scope="scope" >
        
        <el-switch
         v-model="scope.row.status" 
         class="main-el-switch" 
         :active-value="1" 
         :inactive-value="0" 
         active-text="上架" 
         inactive-text="下架"
         @change="handleUpdateStatus(scope.row)"
         ></el-switch>

      </template>


    </el-table-column>

    <el-table-column label="操作" width="300px">
      <template slot-scope="scope">

        <el-button
          size="mini"
          @click="handleEdit(scope.row)">修改</el-button>
        <el-button
          size="mini"
          
          @click="handleDelete(scope.row)">删除</el-button>
      </template>
    </el-table-column>


  </el-table>


  <el-dialog
    title="添加产品系列信息" 
    :visible.sync="isShow.addForm" 
    :before-close="addFormComfirm" 
    :close-on-click-modal="true" 
    :append-to-body="true" 
    width="35%">
      <el-form :model="addForm" :rules="SeriesRules" ref="addForm" label-width="130px" class="demo-addForm">
        <el-form-item label="系列名称" prop="title">
          <el-input v-model="addForm.title" placeholder="请输入系列名称" style="width: 280px"></el-input>
        </el-form-item>
    
        <el-form-item label="系列介绍" prop="description">
          <el-input type="textarea" v-model="addForm.description" placeholder="请输入系列介绍" style="width: 280px"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button
            type="primary"
            @click="valid('addForm')"
            >立即添加</el-button>
        <el-button @click="addFormReset('addForm')">重置</el-button>
        </el-form-item>
    </el-form>
    </el-dialog>




  </div>
</template>

<script>
import { getProductList } from '@/api/Product/Product'
export default {
  created() {
    this.getList()

  },
  methods: {
    getList() {
      getProductList().then(res => {
        this.ProductList = res.data.rows
        
        console.log('this', this.ProductList)
      })
    }


  },
  data() {
    return {
      ProductList: [
      ],
      addForm: {

      },
      isShow: {
        addForm: false
      }
    }
  }

}
</script>

<style>

</style>