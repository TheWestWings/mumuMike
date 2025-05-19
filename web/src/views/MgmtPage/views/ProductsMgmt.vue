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
      prop="productTypeName"
      label="类别"
    ></el-table-column>

    <el-table-column
      prop="pictureUrl"
      label="图片">


      <template slot-scope="scope">
        <img :src="scope.row.pictureUrl" style="width: 50px; height: auto;">
      </template>
    </el-table-column>

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
     title="修改产品信息" 
     :visible.sync="isShow.modifyForm" 
     :before-close="modifyFormComfirm" 
     :close-on-click-modal="true" 
     :append-to-body="true" 
     width="35%">
      <el-form :model="modifyForm" :rules="ProductRules" ref="modifyForm" label-width="130px" class="demo-modifyForm">
        <el-form-item label="系列名称" prop="name">
          <el-input v-model="modifyForm.name" placeholder="请输入系列名称" style="width: 280px"></el-input>
        </el-form-item>
    
        <el-form-item label="系列介绍" prop="description">
          <el-input type="textarea" v-model="modifyForm.description" placeholder="请输入系列介绍" style="width: 280px"></el-input>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model="modifyForm.peice" placeholder="请输入产品价格" style="width: 280px"></el-input>
        </el-form-item>

        <el-form-item label="所属系列" prop="productTypeName">
          <el-input v-model="modifyForm.peice" placeholder="请输入产品所属系列" style="width: 280px"></el-input>
        </el-form-item>
        

        <el-form-item>
            <el-button
            type="primary"
            @click="valid('modifyForm')"
            >立即修改</el-button>
        <el-button @click="modifyFormReset('modifyForm')">重置</el-button>
        </el-form-item>
    </el-form>
    </el-dialog>

<!-- 
  <i
   class="el-icon-circle-plus"
    style="color: #7D665F;
    font-size:80px;"
    @click="add">
  </i> -->



  <!-- <el-dialog
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
    </el-dialog> -->




  </div>
</template>

<script>
import { getProductList, updateProductStatus } from '@/api/Product/Product'
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
    },
    handleUpdateStatus(row) {
      let status = row.status
      updateProductStatus({id: row.id, status: status}).then(() => {
        this.getList()
      })

    },

    ////修改
    
    modifyFormComfirm() {
    {
      this.$confirm('是否保存修改？', '确认信息', {
      closeOnClickModal:false,
      distinguishCancelAndClose: true,
      confirmButtonText: '保存',
      cancelButtonText: '放弃修改'
    })
      .then(() => {

        this.modifyFormSubmit()
        this.handelCloseDialog('modifyForm');
      })
      .catch(action => {
        console.log(action)
        if(action === 'cancel') {
          this.handelCloseDialog('modifyForm');
          this.$message({
            type: 'info',
            message: '取消修改'
          })

        }

      });
    }
    },

    handleEdit() {


    }
    




  },
  data() {
    return {
      ProductList: [
      ],
      addForm: {

      },
      isShow: {
        addForm: false,
        modifyForm: false
      },
      ProductRules: {
        name: [
          { required: true, message: '请输入产品名称', trigger: 'blur' },
          
        ],
        description: [
          { required: true, message: '请输入产品介绍', trigger: 'blur' },
        ],

      },

      modifyForm: {

      },
      modifyFormBufferData: {

      },

    }
  }

}
</script>

<style scope>
  .el-table .mgmter {
    background: rgba(195, 175, 171, 0.2);
  }

  .el-table th {
    font-size: 14px;
  }

  .el-table td {
    font-size: 13px;
  }
</style>

<style scoped lang="less">

.el-table {
  .el-button {
    padding: 8px 15px;
    margin: 0 5px;
    &.el-button--mini {
      font-size: 12px;
    }
    &:hover {
      transform: translateY(-1px);
      transition: all 0.2s;
    }
  }
}

::v-deep .main-el-switch {
    position: relative;
    height: 25px;
    line-height: 30px;

    .el-switch__core {
        height: 25px;
        border-radius: 15px;
        min-width: 64px;
        &:after {
            width: 20px;
            height: 20px;
            left: 2.5px;
            top:2px;
        }
    }

    &.el-switch {
        &.is-checked {
            .el-switch__core {
                &:after {
                    margin-left: -22px;
                    left: 100%;
                }
            }
        }
    }

    &.is-checked {
        .el-switch__label--left {
            opacity: 0;
        }

        .el-switch__label--right {
            opacity: 1;
        }
    }

    .el-switch__label {
        position: absolute;
        top: -3px;
    }

    .el-switch__label--left {
        right: 0;
        color: #999;
        z-index: 1;
        margin-right: 8px;
    }

    .el-switch__label--right {
        left: 0;
        color: #fff;
        opacity: 0;
        margin-left: 8px;
    }
}

</style>
