<template>
  <div>

    <el-table
      :data="SeriesList"
      style="width: calc(100% - 40px); margin: 20px; border-radius: 8px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"
      :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight: 'bold', padding: '12px 0', textAlign: 'center'}"
      :row-style="{height: '60px'}"
      :cell-style="{padding: '0 15px', textAlign: 'center'}"
      >
    <el-table-column
      prop="title"
      label="系列名称"
      >
    </el-table-column>
    <el-table-column
      prop="description"
      label="详细"
      >
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
 
  <i
   class="el-icon-circle-plus"
    style="color: #7D665F;
    font-size:80px;"
    @click="add">
  </i>

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

    <el-dialog
     title="修改产品系列信息" 
     :visible.sync="isShow.modifyForm" 
     :before-close="modifyFormComfirm" 
     :close-on-click-modal="true" 
     :append-to-body="true" 
     width="35%">
      <el-form :model="modifyForm" :rules="SeriesRules" ref="modifyForm" label-width="130px" class="demo-modifyForm">
        <el-form-item label="系列名称" prop="title">
          <el-input v-model="modifyForm.title" placeholder="请输入系列名称" style="width: 280px"></el-input>
        </el-form-item>
    
        <el-form-item label="系列介绍" prop="description">
          <el-input type="textarea" v-model="modifyForm.description" placeholder="请输入系列介绍" style="width: 280px"></el-input>
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

  </div>
</template>

<script>
import { updateStatus, getSeriesList, addSeries, updateSeries, getSeriesById, deleteSeries } from '@/api/Series/Series'


export default {
  mounted()
  {
    this.getList()
  },


  methods: {
    getList() {
      getSeriesList().then(res =>{
        console.log(res)
        this.SeriesList = res.data.rows
      })
    },

    handleUpdateStatus(row) {
      updateStatus({id: row.id, status: row.status}).then(() =>{
        this.getList()
      })
    },
    add() {
      this.isShow.addForm = true
    },
    addFormSubmit() {
      addSeries(this.addForm).then(res => {       
        if(res.data.code === 200) {
          this.getList()
          this.$message({
            type: 'success',
            message: '添加成功！'
          })
        }
      })

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
////关闭页面
    handelCloseDialog(formName) {
      this.isShow[formName] = false
      this.$refs[formName].resetFields();

    },

  //////////////修改产品系列

  handleEdit(row) {
    this.modifyForm.id = row.id
    this.initSeries(this.modifyForm.id)
    
  },
  initSeries(id) {
    getSeriesById(id).then(res => {
      console.log(res)
      
      this.modifyForm.title = res.data.data.title
      this.modifyForm.description = res.data.data.description
      this.modifyFormBufferData = res.data.data
      console.log(this.modifyForm)
      this.isShow.modifyForm = true

    })
    },
    modifyFormSubmit() {
     updateSeries(this.modifyForm).then((response) => {

      if(response.data.code === 200)
        this.$message({
            type: 'success',
            message: '修改成功'
        });

      else if(response.data.code === 500)
      this.$message({
            type: 'error',
            message: '修改失败！产品系列名名已存在'
        });

      this.getList()

    })

    },
  
    modifyFormReset() {
        this.modifyForm.title = this.modifyFormBufferData.title
        this.modifyForm.description = this.modifyFormBufferData.description

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

    ///////////////////表单合法判断
    valid(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(formName === 'addForm')
              this.addFormComfirm()
            else if(formName === 'modifyForm')
              this.modifyFormComfirm()

          } else {
              console.log("no")
          }
        });
    },

    //////////////删除
    del(id) {
      deleteSeries(id).then(() => {
        
        this.getList()
        this.$message({
          type: 'success',
          message: '移除成功！'
        })
      })

    },
    handleDelete(row) {{
      this.$confirm(`确认移除 ${row.title} ？`, '确认信息', {
      closeOnClickModal:false,
      distinguishCancelAndClose: true,
      confirmButtonText: '移除',
      cancelButtonText: '取消移除'
    }).then(() => {
        this.del(row.id)
      })
      .catch(action => {
        if(action === 'close' || action === 'cancel') {
          this.$message({
            type: 'info',
            message: '取消移除'
          })
        }
      });



    }}



  },


  data() {
    return {
      SeriesList: [
        {
          
        }
      ],
      isShow: {
        addForm: false,
        modifyForm: false
      },

      SeriesRules: {
        title: [
          { required: true, message: '请输入水库名称', trigger: 'blur' },
          
        ],
        description: [
          { required: true, message: '请输入水库所属河流', trigger: 'blur' },
        ],
      },

      addForm: {

      },
      modifyForm: {
        title: '',
        description: '',
        id: '',
      },
      modifyFormBufferData: {
        title: '',
        description: '',
        id: '',        

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
/* .dotClass {
  width:10px;
  height:10px;
  border-radius: 50%;
  display: block;
  margin-left: 10px;
} */



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