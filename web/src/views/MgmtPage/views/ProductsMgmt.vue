<template>
  <div>
    <h2 class="page-title">具体产品管理</h2>
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
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="modifyForm.name" placeholder="请输入产品名称" style="width: 280px"></el-input>
        </el-form-item>
    
        <el-form-item label="产品介绍" prop="description">
          <el-input type="textarea" v-model="modifyForm.description" placeholder="请输入系列介绍" style="width: 280px"></el-input>
        </el-form-item>

        <el-form-item label="产品价格" prop="price">
          <el-input v-model="modifyForm.price" placeholder="请输入产品价格" style="width: 280px"></el-input>
        </el-form-item>



        <el-form-item label="所属系列" prop="productTypeName">
          <el-select v-model="modifyForm.productTypeId" filterable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.title"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="产品图片" prop="pictureUrl">
          <el-upload
            action
            :auto-upload="false"
            :file-list="fileList"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleFileChange"
            :class="{styleB:fileList.length === 1}">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>

        </el-form-item>
        

        <el-form-item>
            <el-button
            type="primary"
            @click="handlerUpdateSubmit()"
            >立即修改</el-button>
        <el-button @click="modifyFormReset()">重置</el-button>
        </el-form-item>
    </el-form>
    </el-dialog>


    <i
    class="el-icon-circle-plus"
        style="color: #7D665F;
        font-size:80px;"
        @click="add">
    </i>

  <el-dialog
    title="添加产品产品信息" 
    :visible.sync="isShow.addForm" 
    :before-close="addFormComfirm" 
    :close-on-click-modal="true" 
    :append-to-body="true" 
    width="35%">
      <el-form :model="addForm" :rules="ProductRules" ref="addForm" label-width="130px" class="demo-modifyForm">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入产品名称" style="width: 280px"></el-input>
        </el-form-item>
    
        <el-form-item label="产品介绍" prop="description">
          <el-input type="textarea" v-model="addForm.description" placeholder="请输入产品介绍" style="width: 280px"></el-input>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model="addForm.price" placeholder="请输入产品价格" style="width: 280px"></el-input>
        </el-form-item>



        <el-form-item label="所属系列" prop="productTypeName">
          <el-select v-model="addForm.productTypeId" filterable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.title"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="产品图片" prop="pictureUrl" class="styleB">
          <el-upload
            action
            :auto-upload="false"
            :file-list="fileList"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleFileChange"
            class="styleB">
            
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>

        </el-form-item>
        

        <el-form-item>
            <el-button
            type="primary"
            @click="handlerAddSubmit()"
            >立即修改</el-button>
        <el-button @click="modifyFormReset()">重置</el-button>
        </el-form-item>
    </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getProductList, updateProductStatus, getProductById, updateProduct, addProduct, deleteProduct } from '@/api/Product/Product'
import { getSeriesList } from '@/api/Series/Series'

export default {
  created() {
    this.getList()
    getSeriesList().then(res => {
      this.options = res.data.rows
    })

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

    ////关闭页面
    handelCloseDialog(formName) {
      this.isShow[formName] = false
      this.$refs[formName].resetFields();
      this.fileList = []
      this.addForm = {}
      this.modifyForm = {}
    },

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 修改产品
    handleEdit(row) {
      this.modifyForm.id = row.id
      this.initSeries(this.modifyForm.id)
    },
    initSeries(id) {
        getProductById(id).then(res => {
    
        this.modifyForm = res.data.data
        this.modifyFormBufferData = JSON.parse(JSON.stringify(this.modifyForm))
        this.fileList = [{
            name: this.modifyForm.name,
            url: this.modifyForm.pictureUrl
        }]
        console.log(this.fileList)
        this.isShow.modifyForm = true
        })
    },
    handleFileChange(file) {
      // 当文件改变时更新fileList
      if (file.raw) {
        this.fileList = [{
          name: file.name,
          url: URL.createObjectURL(file.raw),
          raw: file.raw
        }]
      }
    },

    handleRemove() {
      // 清空文件列表
      this.fileList = []
    },

    handlerUpdateSubmit() {
      this.$refs.modifyForm.validate((valid) => {
        if (valid) {
          this.modifyFormComfirm()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handlerAddSubmit() {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.addFormComfirm()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    handleDelete(row) {
      this.$confirm('是否删除该产品？', '确认信息', {
        closeOnClickModal:false,
        distinguishCancelAndClose: true,
        confirmButtonText: '删除',
        cancelButtonText: '取消'
      })
      .then(() => {
        this.deleteProduct(row)
      })
      .catch(action => {
          console.log(action)
          if(action === 'cancel') {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          }
      });
    },

    deleteProduct(row) {
      deleteProduct(row.id).then(() => {
        this.getList()
        this.$message({
          type: 'success',
          message: '删除成功'
        })
      })
    },
    

    addFormSubmit() {
      const data = new FormData()
      
      data.append("name", this.addForm.name)
      data.append("description", this.addForm.description)
      data.append("price", this.addForm.price)
      data.append("productTypeId", this.addForm.productTypeId)

      // 检查是否有文件需要上传
      if (this.fileList.length > 0 && this.fileList[0].raw) {
        data.append("image", this.fileList[0].raw)
      }

     addProduct(data).then((res) => {

      if(res.data.code === 200)
        this.$message({
            type: 'success',
            message: '添加成功'
        });

      else if(res.data.code === 500)
      this.$message({
            type: 'error',
            message: '添加失败！产品系列名名已存在'
        });
      this.getList()

    })

    },

    modifyFormSubmit() {
      const data = new FormData()
      
      data.append("id", this.modifyForm.id)
      data.append("name", this.modifyForm.name)
      data.append("description", this.modifyForm.description)
      data.append("price", this.modifyForm.price)
      data.append("productTypeId", this.modifyForm.productTypeId)

      // 检查是否有文件需要上传
      if (this.fileList.length > 0 && this.fileList[0].raw) {
        data.append("image", this.fileList[0].raw)
      }

     updateProduct(data).then((res) => {

      if(res.data.code === 200)
        this.$message({
            type: 'success',
            message: '修改成功'
        });

      else if(res.data.code === 500)
      this.$message({
            type: 'error',
            message: '修改失败！产品系列名名已存在'
        });

      this.getList()

    })

    },
  
    modifyFormReset() {

      console.log(this.modifyFormBufferData)
        this.modifyForm = JSON.parse(JSON.stringify(this.modifyFormBufferData))
        this.fileList = [{
            name: this.modifyForm.name,
            url: this.modifyForm.pictureUrl
        }]

    },
    addFormComfirm() {
      this.$confirm('是否保存添加？', '确认信息', {
      closeOnClickModal:false,
      distinguishCancelAndClose: true,
      confirmButtonText: '保存',
      cancelButtonText: '放弃添加'
      })
      .then(() => {
        this.addFormSubmit()
        this.handelCloseDialog('addForm');
        
      })
      .catch(action => {
          console.log(action)
          if(action === 'cancel') {
            this.handelCloseDialog('addForm')
            
            this.$message({
              type: 'info',
              message: '取消添加'
            })
          }
      });
    },

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
    add(){
        this.isShow.addForm = true
    }
  },
  data() {
    return {
      // 图片上传
      dialogImageUrl: '',
      dialogVisible: false,
      fileList:[],

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
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
  border-left: 4px solid #409EFF;
  padding-left: 12px;
}
  .el-table .mgmter {
    background: rgba(195, 175, 171, 0.2);
  }

  .el-table th {
    font-size: 14px;
  }

  .el-table td {
    font-size: 13px;
  }

  /* 针对 el-table 单元格内的 .cell 元素 */
.el-table .el-table__body .cell {
    height: 100px; /* 设置 .cell 元素高度 */
    display: flex; /* 使用 Flex 布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    padding: 0; /* 移除默认内边距 */
}

/* 确保 td 高度适应 .cell */
.el-table .el-table__body td {
    height: 100px; /* 与 .cell 高度一致 */
    padding: 0; /* 移除 td 的默认内边距 */
}


.styleB .el-upload--picture-card{
    display:none;   
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
