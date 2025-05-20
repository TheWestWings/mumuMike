<template>
  <div>
    <el-upload
      action="#"
      list-type="picture-card"
      :auto-upload="false"
      :limit="1"
      :on-change="handleChange"
      :on-exceed="handleExceed"
      >
        <!-- <i slot="default" class="el-icon-plus"></i> -->
        <img :src="fileURL" slot="default">
        <div slot="file" slot-scope="{file}">
          <!-- 上传的照片 -->
          <img
            class="el-upload-list__item-thumbnail"
            :src="file.url" alt=""
          >
          <span class="el-upload-list__item-actions">
            <span
              class="el-upload-list__item-preview"
              @click="handlePictureCardPreview(file)"
            >
              <i class="el-icon-zoom-in"></i>
            </span>

            <span
              
              class="el-upload-list__item-delete"
              @click="handleModify(file)"
            >
              <i class="el-icon-edit-outline"></i>
            </span>
          </span>
        </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
  
  
</template>

<script>
export default {
  methods: {

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleModify() {

    },

    handleChange(file) {
      this.fileURL = file.url

    },
    handleExceed(file, fileList){
      fileList = []
      fileList.push(file)
      
    }

  },
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      fileURL: "src\\assets\\background\\login1.png"
    }
  }

}
</script>

<style scoped>
/*当upLoadShow为true时，启用如下样式，即上传框的样式，若为false则不启用该样式*/
  .upLoadShow .el-upload {
    width: 30rem !important;
    height: 30rem !important;
    line-height: 30rem !important;
  }

/*当upLoadHide为true时，启用如下样式，即缩略图的样式，若为false则不启用该样式*/
  .upLoadHide .el-upload-list--picture-card .el-upload-list__item {
    width: 30rem !important;
    height: 30rem !important;
    line-height: 30rem !important;
  }
/*当upLoadHide为true时，启用如下样式，即上传框的样式，若为false则不启用该样式*/
  .upLoadHide .el-upload {
    display: none;
  }

</style>