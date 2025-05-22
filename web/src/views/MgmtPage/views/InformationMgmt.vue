<template>
  <div class="information-mgmt-container" v-loading="loading" element-loading-text="正在加载信息数据...">
    <h2 class="page-title">留言管理</h2>
    
    <!-- 状态切换标签 -->
    <div class="status-tabs">
      <el-radio-group v-model="currentStatus" @change="handleStatusChange">
        <el-radio-button :label="0">待回复</el-radio-button>
        <el-radio-button :label="1">已回复</el-radio-button>
      </el-radio-group>
    </div>
    
    <!-- 空状态显示 -->
    <div v-if="informationList.length === 0" class="empty-state">
      <i class="el-icon-message empty-icon"></i>
      <p>{{ currentStatus === 0 ? '暂无待回复信息' : '暂无已回复信息' }}</p>
    </div>
    
    <!-- 信息列表 -->
    <el-collapse 
      class="info-collapse"
      v-for="(item, index) in informationList"
      :key="item.id"
      v-model="activeNames">
      <el-collapse-item :name="index">
        <template slot="title">
          <div class="collapse-title">
            <span class="info-id">ID: {{ item.id }}</span>
            <span class="info-title">{{ "主题: " + item.title }}</span>
            <span class="info-time">{{ "创建时间: " + formatDate(item.createTime) }}</span>
            <span class="info-time">{{ "回复时间: " + formatDate(item.updateTime) }}</span>
            <el-tag 
              class="info-status-tag" 
              :type="getStatusType(item.status)" 
              size="small"
              effect="dark"
            >
              {{ statusText[item.status] }}
            </el-tag>
          </div>
        </template>
        
        <el-descriptions
          class="info-description" 
          :column="2" 
          border
          size="medium"
        >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              用户信息
            </template>
            <div class="user-info">
              <p><span class="label">用户ID:</span> {{ item.userId }}</p>
              <p v-if="item.user"><span class="label">用户名:</span> {{ item.user.username }}</p>
            </div>
          </el-descriptions-item>

          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-date"></i>
              时间信息
            </template>
            <div class="time-info">
              <p><span class="label">创建时间:</span> {{ formatDate(item.createTime) }}</p>
              <p v-if="item.updateTime && item.status === 1"><span class="label">回复时间:</span> {{ formatDate(item.updateTime) }}</p>
            </div>
          </el-descriptions-item>

          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-chat-line-square"></i>
              信息内容
            </template>
            <div class="content-box">
              {{ item.content }}
            </div>
          </el-descriptions-item>

          <el-descriptions-item v-if="item.status === 1" :span="2">
            <template slot="label">
              <i class="el-icon-s-comment"></i>
              回复内容
            </template>
            <div class="reply-box">
              {{ item.reply }}
            </div>
          </el-descriptions-item>

          <el-descriptions-item v-if="item.status === 0" :span="2">
            <template slot="label">
              <i class="el-icon-edit-outline"></i>
              回复信息
            </template>
            <div class="reply-form">
              <el-input
                type="textarea"
                :rows="4"
                placeholder="请输入回复内容"
                v-model="replyForm[item.id]"
              ></el-input>
              <div class="form-actions">
                <el-button type="primary" @click="handleReply(item)" :loading="submitting === item.id">
                  提交回复
                </el-button>
              </div>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import { getInformationVOList, updateInformation } from '@/api/Information/Information'

export default {
  mounted() {
    this.getList(true)
    // 添加定时器，每60秒自动刷新列表数据，不显示加载状态
    this.timer = setInterval(() => {
      this.getList(false)
    }, 1000) // 设置为10秒，避免频繁请求服务器
  },
  beforeDestroy() {
    // 组件销毁前清除定时器
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    getList(showLoading = true) {
      if (showLoading) {
        this.loading = true
      }
      getInformationVOList({ status: this.currentStatus }).then((res) => {
        this.informationList = res.data.rows || []
        // 初始化回复表单
        this.informationList.forEach(item => {
          if (item.status === 0 && !this.replyForm[item.id]) {
            this.$set(this.replyForm, item.id, '')
          }
        })
      }).finally(() => {
        if (showLoading) {
          this.loading = false
        }
      })
    },
    handleStatusChange() {
      this.activeNames = []
      this.getList(true)
    },
    handleReply(item) {
      if (!this.replyForm[item.id] || this.replyForm[item.id].trim() === '') {
        this.$message.warning('回复内容不能为空')
        return
      }

      this.submitting = item.id
      
      const data = {
        id: item.id,
        reply: this.replyForm[item.id],
        status: 1 // 更新为已回复状态
      }
      
      updateInformation(data).then(() => {
        this.$message.success('回复成功')
        this.replyForm[item.id] = ''
        this.getList(true) // 回复成功后显示加载状态刷新列表
      }).catch(err => {
        this.$message.error('回复失败: ' + (err.message || '未知错误'))
      }).finally(() => {
        this.submitting = null
      })
    },
    // 格式化日期为 YYYY-MM-DD HH:MM:SS 格式
    formatDate(dateString) {
      if (!dateString) return '暂无'
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
    },
    getStatusType(status) {
      return this.statusTypeMap[status] || 'info'
    }
  },
  data() {
    return {
      loading: false,
      currentStatus: 0, // 默认显示待回复
      informationList: [],
      statusText: ['待回复', '已回复'],
      statusTypeMap: {
        0: 'warning',  // 待回复
        1: 'success'   // 已回复
      },
      activeNames: [],
      replyForm: {}, // 存储每个信息项的回复内容
      submitting: null, // 正在提交回复的信息ID
      timer: null // 存储定时器ID
    }
  }
}
</script>

<style scoped>
.information-mgmt-container {
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

.status-tabs {
  margin-bottom: 20px;
  text-align: center;
}

.info-collapse {
  margin-bottom: 16px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.info-collapse /deep/ .el-collapse-item__header {
  background-color: #f5f7fa;
  padding: 0 20px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  height: 50px;
  line-height: 50px;
}

.info-collapse /deep/ .el-collapse-item__content {
  padding: 15px;
}

.collapse-title {
  display: flex;
  align-items: center;
  width: 100%;
}

.info-id {
  font-weight: 600;
  color: #8d6e63;
  margin-right: 15px;
}

.info-title {
  font-weight: 500;
  color: #303133;
  margin-right: 15px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
}

.info-time {
  color: #606266;
  margin-right: 15px;
  font-size: 14px;
  flex-grow: 1;
}

.info-status-tag {
  margin-left: auto;
}

.info-description {
  margin: 0;
}

.info-description /deep/ .el-descriptions__header {
  display: none;
}

.info-description /deep/ .el-descriptions-item__label {
  background-color: #f5f7fa;
  padding: 12px 15px;
}

.info-description /deep/ .el-descriptions-item__content {
  padding: 12px 15px;
}

.label {
  font-weight: 500;
  color: #606266;
  margin-right: 8px;
}

.content-box, .reply-box {
  background-color: #f8f8f8;
  padding: 15px;
  border-radius: 4px;
  min-height: 60px;
  white-space: pre-wrap;
  word-break: break-word;
}

.reply-box {
  background-color: #e8f5e9;
}

.content-box {
  border-left: 3px solid #409EFF;
}

.reply-box {
  border-left: 3px solid #67C23A;
}

.reply-form {
  margin-top: 10px;
}

.form-actions {
  margin-top: 10px;
  text-align: right;
}

.user-info p, .time-info p {
  margin: 5px 0;
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
  
  .info-title, .info-time {
    margin: 5px 0;
    max-width: 100%;
  }
  
  .info-status-tag {
    margin-left: 0;
    margin-top: 5px;
  }
  
  .info-description /deep/ .el-descriptions-item {
    width: 100%;
  }
}
</style>