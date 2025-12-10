<template>
  <div class="table-qrcode-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>餐桌二维码管理</h2>
      <p>生成餐桌二维码，顾客扫码后可直接进入点餐页面</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="batch-generate">
        <span>批量生成：</span>
        <el-input-number v-model="startNo" :min="1" :max="100" label="起始桌号" size="small" />
        <span style="margin: 0 10px;">至</span>
        <el-input-number v-model="endNo" :min="startNo" :max="100" label="结束桌号" size="small" />
        <el-button type="primary" @click="batchGenerate" style="margin-left: 15px;">
          批量生成
        </el-button>
      </div>
      <div class="actions">
        <el-button type="success" @click="downloadAll" :disabled="tableList.length === 0">
          下载全部
        </el-button>
      </div>
    </div>

    <!-- 二维码列表 -->
    <div class="qrcode-grid" v-if="tableList.length > 0">
      <div class="qrcode-card" v-for="table in tableList" :key="table.tableNo">
        <div class="qrcode-image">
          <img :src="getQRCodeUrl(table.tableNo)" :alt="'桌号' + table.tableNo" />
        </div>
        <div class="qrcode-info">
          <span class="table-no">{{ table.tableNo }} 号桌</span>
        </div>
        <div class="qrcode-actions">
          <el-button size="small" type="primary" @click="downloadQRCode(table.tableNo)">
            下载
          </el-button>
          <el-button size="small" @click="previewQRCode(table.tableNo)">
            预览
          </el-button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-else>
      <el-empty description="暂无餐桌二维码，请先生成">
        <el-button type="primary" @click="batchGenerate">立即生成</el-button>
      </el-empty>
    </div>

    <!-- 预览弹窗 -->
    <el-dialog :visible.sync="previewVisible" title="二维码预览" width="400px" center>
      <div class="preview-content">
        <img :src="getQRCodeUrl(previewTableNo)" :alt="'桌号' + previewTableNo" class="preview-image" />
        <div class="preview-info">
          <p class="table-label">{{ previewTableNo }} 号桌</p>
          <p class="scan-hint">扫描二维码进入点餐页面</p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="downloadQRCode(previewTableNo)">下载二维码</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TableQRCode',
  data() {
    return {
      startNo: 1,
      endNo: 10,
      tableList: [],
      previewVisible: false,
      previewTableNo: 1
    }
  },
  mounted() {
    this.batchGenerate()
  },
  methods: {
    // 获取API基础URL
    getBaseUrl() {
      return process.env.VUE_APP_API_URL || 'http://localhost:8080'
    },

    // 获取二维码图片URL
    getQRCodeUrl(tableNo) {
      return `${this.getBaseUrl()}/table/qrcode/${tableNo}`
    },

    // 批量生成二维码
    batchGenerate() {
      if (this.startNo > this.endNo) {
        this.$message.warning('起始桌号不能大于结束桌号')
        return
      }
      
      const list = []
      for (let i = this.startNo; i <= this.endNo; i++) {
        list.push({
          tableNo: i,
          qrCodeUrl: this.getQRCodeUrl(i)
        })
      }
      this.tableList = list
      this.$message.success(`已生成 ${list.length} 个餐桌二维码`)
    },

    // 下载单个二维码
    async downloadQRCode(tableNo) {
      try {
        const response = await fetch(this.getQRCodeUrl(tableNo))
        const blob = await response.blob()
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `餐桌${tableNo}号二维码.png`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success(`桌号${tableNo}二维码下载成功`)
      } catch (error) {
        console.error('下载失败', error)
        this.$message.error('下载失败，请重试')
      }
    },

    // 下载全部二维码
    async downloadAll() {
      this.$message.info('正在下载全部二维码，请稍候...')
      for (const table of this.tableList) {
        await this.downloadQRCode(table.tableNo)
        // 添加延迟避免浏览器阻止
        await new Promise(resolve => setTimeout(resolve, 300))
      }
      this.$message.success('全部下载完成')
    },

    // 预览二维码
    previewQRCode(tableNo) {
      this.previewTableNo = tableNo
      this.previewVisible = true
    }
  }
}
</script>

<style scoped>
.table-qrcode-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.batch-generate {
  display: flex;
  align-items: center;
}

.batch-generate span {
  color: #606266;
  font-size: 14px;
}

.qrcode-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.qrcode-card {
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  transition: box-shadow 0.3s;
}

.qrcode-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.qrcode-image {
  width: 160px;
  height: 160px;
  margin: 0 auto 15px;
}

.qrcode-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qrcode-info {
  margin-bottom: 15px;
}

.table-no {
  font-size: 16px;
  font-weight: bold;
  color: #5d4037;
}

.qrcode-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.empty-state {
  padding: 60px 0;
}

/* 预览弹窗样式 */
.preview-content {
  text-align: center;
}

.preview-image {
  width: 280px;
  height: 280px;
  margin-bottom: 20px;
}

.preview-info {
  text-align: center;
}

.preview-info .table-label {
  font-size: 24px;
  font-weight: bold;
  color: #5d4037;
  margin: 0 0 10px 0;
}

.preview-info .scan-hint {
  color: #909399;
  font-size: 14px;
  margin: 0;
}
</style>
