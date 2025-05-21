<template>
  <div>
    <h2 class="page-title">用户管理</h2>
    <el-table
      :data="userList"
      style="width: calc(100% - 40px); margin: 20px; border-radius: 8px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"
      :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight: 'bold', padding: '12px 0', textAlign: 'center'}"
      :row-style="{height: '60px'}"
      :cell-style="{padding: '0 15px', textAlign: 'center'}"
      :row-class-name="tableRowClassName">
    <el-table-column
      prop="username"
      label="用户名"
      >
    </el-table-column>
    <el-table-column
      prop="phone"
      label="电话"
      >
    </el-table-column>

    <el-table-column
      prop="email"
      label="邮箱">
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
         active-value="1" 
         inactive-value="0" 
         active-text="启用" 
         inactive-text="禁用"
         @change="handleUpdateStatus(scope.row)"
         ></el-switch>

      </template>


    </el-table-column>
    <el-table-column label="操作" width="300px">
      <template slot-scope="scope">

        <span slot="reference" v-if="scope.row.role == 2 && $store.state.role == 0">
          <el-button
             plain
            size="mini"
            @click="handleSetRole(scope.row)">设为管理员</el-button>&ensp;
        </span>
        <span slot="reference" v-if="scope.row.role == 1 && $store.state.role == 0">
            <el-button
            plain
            size="mini"
            @click="handleSetRole(scope.row)">取消管理员</el-button>&ensp;
        </span>
     

        <el-button
          size="mini"
          @click="handleEdit(scope.$index, scope.row)">修改</el-button>
        <el-button
          size="mini"
          
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>


  </el-table>

  <!-- 用户信息修改对话框 -->
  <el-dialog
    title="修改用户信息"
    :visible.sync="dialogVisible"
    width="30%"
    :close-on-click-modal="false"
    @closed="resetForm">
    <el-form :model="editForm" ref="editForm" label-width="80px" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="editForm.username" placeholder="请输入用户名" readonly></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="editForm.phone" placeholder="请输入电话号码"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="editForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </span>
  </el-dialog>
  </div>
</template>

<script>
import { getUserList, updateRole, updateStatus, getUserById, updateUserInfo } from '@/api/User/User'


export default {
  created()
  {
    this.getList()
  },


  methods: {
    getList() {
        let data = {}
        if(this.$store.state.role == 0){
            data = {}
        }
        else data = {role : 2}
      getUserList(data).then(res => { 
      this.userList = res.data.rows 
      console.log(res.data.rows)
      console.log(this.userList)
    })

    },
    tableRowClassName({row}) {
      
      if(row.role === 1){
        console.log('row' ,row)

        return 'mgmter'
      }else{
        return ''
      }
    },
    handleSetRole(row) {

      row.role = row.role === 1? 2: 1
      
      let user = {id: row.id, role: row.role}

      updateRole(user).then(() => {
        this.getList()
      })

    },
    handleUpdateStatus(row) {
      updateStatus({id: row.id, status: row.status}).then(() =>{
        this.getList()
      })
    },
    // 处理编辑操作
    handleEdit(index, row) {
      // 打开对话框
      this.dialogVisible = true
      this.currentUserId = row.id
      
      // 获取用户详细信息
      getUserById(row.id).then(res => {
        if (res.data.code === 200) {
          // 填充表单
          this.editForm = {
            username: res.data.data.username,
            phone: res.data.data.phone,
            email: res.data.data.email
          }
        } else {
          this.$message.error('获取用户信息失败')
        }
      })
    },
    // 提交表单
    submitForm() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          // 组装更新数据
          const updateData = {
            id: this.currentUserId,
            phone: this.editForm.phone,
            email: this.editForm.email
          }
          
          // 调用更新API
          updateUserInfo(updateData).then(res => {
            if (res.data.code === 200) {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.getList() // 刷新列表
            } else {
              this.$message.error(res.msg || '修改失败')
            }
          }).catch(err => {
            this.$message.error('修改失败：' + err.message)
          })
        }
      })
    },
    // 重置表单
    resetForm() {
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields()
      }
      this.editForm = {
        username: '',
        phone: '',
        email: ''
      }
    },
    // 处理删除操作
    handleDelete(_index, row) {
      this.$confirm('确认删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里应该调用删除API
        console.log('删除用户ID:', row.id)
        this.$message.success('删除成功')
        this.getList() // 刷新列表
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  },


  data() {
    return {
      userList: [
        {
          
        }
      ],
      // 对话框显示控制
      dialogVisible: false,
      // 当前编辑的用户ID
      currentUserId: null,
      // 编辑表单数据
      editForm: {
        username: '',
        phone: '',
        email: ''
      },
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
        ]
      }
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