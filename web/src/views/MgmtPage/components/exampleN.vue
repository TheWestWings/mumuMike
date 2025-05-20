<template>
  <body>
    <el-col span="12" :push="7">
      <!-- auto-upload为是否自动加载；action为图片要上传到的地址，这里随便填一个，因为暂时不用 -->
<!-- class为动态样式选择，是实现上传图片后上传框消失的关键 -->
      <el-upload action="#" 
        list-type="picture-card" 
        :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove" 
        :auto-upload="false" 
        :on-change="handleChange" 
        :class="objClass"
        :file-list="fileList" 
        :limit="1">
        <i class="el-icon-plus"></i>
      </el-upload>
      <!-- el-dialog为点击预览图的放大按钮后弹出来的框，在框内显示放大的图片 -->
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </el-col>
  </body>
</template>

<script>
  export default {
    data() {
      return {
        dialogImageUrl: '',
        dialogVisible: false,
        fileList: [],
        objClass: {
          upLoadShow: true,
          upLoadHide: false,
        },
      };
    },
    methods: {
      handleChange() {
        this.objClass.upLoadHide = true;//上传图片后置upLoadHide为真，隐藏上传框
        this.objClass.upLoadShow = false;
      },
      handleRemove() {
        this.objClass.upLoadShow = true;//删除图片后显示上传框
        this.objClass.upLoadHide = false;
      },
      // 点击预览图的放大按钮后会触发handlePictureCardPreview
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
},
   }
</script>

<style>
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