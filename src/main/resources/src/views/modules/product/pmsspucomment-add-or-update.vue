<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="sku_id" prop="skuId">
      <el-input v-model="dataForm.skuId" placeholder="sku_id"></el-input>
    </el-form-item>
    <el-form-item label="spu_id" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="spu_id"></el-input>
    </el-form-item>
    <el-form-item label="��Ʒ����" prop="spuName">
      <el-input v-model="dataForm.spuName" placeholder="��Ʒ����"></el-input>
    </el-form-item>
    <el-form-item label="��Ա�ǳ�" prop="memberNickName">
      <el-input v-model="dataForm.memberNickName" placeholder="��Ա�ǳ�"></el-input>
    </el-form-item>
    <el-form-item label="�Ǽ�" prop="star">
      <el-input v-model="dataForm.star" placeholder="�Ǽ�"></el-input>
    </el-form-item>
    <el-form-item label="��Աip" prop="memberIp">
      <el-input v-model="dataForm.memberIp" placeholder="��Աip"></el-input>
    </el-form-item>
    <el-form-item label="����ʱ��" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="����ʱ��"></el-input>
    </el-form-item>
    <el-form-item label="��ʾ״̬[0-����ʾ��1-��ʾ]" prop="showStatus">
      <el-input v-model="dataForm.showStatus" placeholder="��ʾ״̬[0-����ʾ��1-��ʾ]"></el-input>
    </el-form-item>
    <el-form-item label="����ʱ�������" prop="spuAttributes">
      <el-input v-model="dataForm.spuAttributes" placeholder="����ʱ�������"></el-input>
    </el-form-item>
    <el-form-item label="������" prop="likesCount">
      <el-input v-model="dataForm.likesCount" placeholder="������"></el-input>
    </el-form-item>
    <el-form-item label="�ظ���" prop="replyCount">
      <el-input v-model="dataForm.replyCount" placeholder="�ظ���"></el-input>
    </el-form-item>
    <el-form-item label="����ͼƬ/��Ƶ[json���ݣ�[{type:�ļ�����,url:��Դ·��}]]" prop="resources">
      <el-input v-model="dataForm.resources" placeholder="����ͼƬ/��Ƶ[json���ݣ�[{type:�ļ�����,url:��Դ·��}]]"></el-input>
    </el-form-item>
    <el-form-item label="����" prop="content">
      <el-input v-model="dataForm.content" placeholder="����"></el-input>
    </el-form-item>
    <el-form-item label="�û�ͷ��" prop="memberIcon">
      <el-input v-model="dataForm.memberIcon" placeholder="�û�ͷ��"></el-input>
    </el-form-item>
    <el-form-item label="��������[0 - ����Ʒ��ֱ�����ۣ�1 - �����۵Ļظ�]" prop="commentType">
      <el-input v-model="dataForm.commentType" placeholder="��������[0 - ����Ʒ��ֱ�����ۣ�1 - �����۵Ļظ�]"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          skuId: '',
          spuId: '',
          spuName: '',
          memberNickName: '',
          star: '',
          memberIp: '',
          createTime: '',
          showStatus: '',
          spuAttributes: '',
          likesCount: '',
          replyCount: '',
          resources: '',
          content: '',
          memberIcon: '',
          commentType: ''
        },
        dataRule: {
          skuId: [
            { required: true, message: 'sku_id不能为空', trigger: 'blur' }
          ],
          spuId: [
            { required: true, message: 'spu_id不能为空', trigger: 'blur' }
          ],
          spuName: [
            { required: true, message: '��Ʒ����不能为空', trigger: 'blur' }
          ],
          memberNickName: [
            { required: true, message: '��Ա�ǳ�不能为空', trigger: 'blur' }
          ],
          star: [
            { required: true, message: '�Ǽ�不能为空', trigger: 'blur' }
          ],
          memberIp: [
            { required: true, message: '��Աip不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '����ʱ��不能为空', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: '��ʾ״̬[0-����ʾ��1-��ʾ]不能为空', trigger: 'blur' }
          ],
          spuAttributes: [
            { required: true, message: '����ʱ�������不能为空', trigger: 'blur' }
          ],
          likesCount: [
            { required: true, message: '������不能为空', trigger: 'blur' }
          ],
          replyCount: [
            { required: true, message: '�ظ���不能为空', trigger: 'blur' }
          ],
          resources: [
            { required: true, message: '����ͼƬ/��Ƶ[json���ݣ�[{type:�ļ�����,url:��Դ·��}]]不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '����不能为空', trigger: 'blur' }
          ],
          memberIcon: [
            { required: true, message: '�û�ͷ��不能为空', trigger: 'blur' }
          ],
          commentType: [
            { required: true, message: '��������[0 - ����Ʒ��ֱ�����ۣ�1 - �����۵Ļظ�]不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/product/pmsspucomment/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.skuId = data.pmsSpuComment.skuId
                this.dataForm.spuId = data.pmsSpuComment.spuId
                this.dataForm.spuName = data.pmsSpuComment.spuName
                this.dataForm.memberNickName = data.pmsSpuComment.memberNickName
                this.dataForm.star = data.pmsSpuComment.star
                this.dataForm.memberIp = data.pmsSpuComment.memberIp
                this.dataForm.createTime = data.pmsSpuComment.createTime
                this.dataForm.showStatus = data.pmsSpuComment.showStatus
                this.dataForm.spuAttributes = data.pmsSpuComment.spuAttributes
                this.dataForm.likesCount = data.pmsSpuComment.likesCount
                this.dataForm.replyCount = data.pmsSpuComment.replyCount
                this.dataForm.resources = data.pmsSpuComment.resources
                this.dataForm.content = data.pmsSpuComment.content
                this.dataForm.memberIcon = data.pmsSpuComment.memberIcon
                this.dataForm.commentType = data.pmsSpuComment.commentType
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/product/pmsspucomment/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'skuId': this.dataForm.skuId,
                'spuId': this.dataForm.spuId,
                'spuName': this.dataForm.spuName,
                'memberNickName': this.dataForm.memberNickName,
                'star': this.dataForm.star,
                'memberIp': this.dataForm.memberIp,
                'createTime': this.dataForm.createTime,
                'showStatus': this.dataForm.showStatus,
                'spuAttributes': this.dataForm.spuAttributes,
                'likesCount': this.dataForm.likesCount,
                'replyCount': this.dataForm.replyCount,
                'resources': this.dataForm.resources,
                'content': this.dataForm.content,
                'memberIcon': this.dataForm.memberIcon,
                'commentType': this.dataForm.commentType
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
