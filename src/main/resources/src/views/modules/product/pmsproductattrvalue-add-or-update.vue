<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="��Ʒid" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="��Ʒid"></el-input>
    </el-form-item>
    <el-form-item label="����id" prop="attrId">
      <el-input v-model="dataForm.attrId" placeholder="����id"></el-input>
    </el-form-item>
    <el-form-item label="������" prop="attrName">
      <el-input v-model="dataForm.attrName" placeholder="������"></el-input>
    </el-form-item>
    <el-form-item label="����ֵ" prop="attrValue">
      <el-input v-model="dataForm.attrValue" placeholder="����ֵ"></el-input>
    </el-form-item>
    <el-form-item label="˳��" prop="attrSort">
      <el-input v-model="dataForm.attrSort" placeholder="˳��"></el-input>
    </el-form-item>
    <el-form-item label="����չʾ���Ƿ�չʾ�ڽ����ϣ�0-�� 1-�ǡ�" prop="quickShow">
      <el-input v-model="dataForm.quickShow" placeholder="����չʾ���Ƿ�չʾ�ڽ����ϣ�0-�� 1-�ǡ�"></el-input>
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
          spuId: '',
          attrId: '',
          attrName: '',
          attrValue: '',
          attrSort: '',
          quickShow: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: '��Ʒid不能为空', trigger: 'blur' }
          ],
          attrId: [
            { required: true, message: '����id不能为空', trigger: 'blur' }
          ],
          attrName: [
            { required: true, message: '������不能为空', trigger: 'blur' }
          ],
          attrValue: [
            { required: true, message: '����ֵ不能为空', trigger: 'blur' }
          ],
          attrSort: [
            { required: true, message: '˳��不能为空', trigger: 'blur' }
          ],
          quickShow: [
            { required: true, message: '����չʾ���Ƿ�չʾ�ڽ����ϣ�0-�� 1-�ǡ�不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/pmsproductattrvalue/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.pmsProductAttrValue.spuId
                this.dataForm.attrId = data.pmsProductAttrValue.attrId
                this.dataForm.attrName = data.pmsProductAttrValue.attrName
                this.dataForm.attrValue = data.pmsProductAttrValue.attrValue
                this.dataForm.attrSort = data.pmsProductAttrValue.attrSort
                this.dataForm.quickShow = data.pmsProductAttrValue.quickShow
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
              url: this.$http.adornUrl(`/product/pmsproductattrvalue/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'spuId': this.dataForm.spuId,
                'attrId': this.dataForm.attrId,
                'attrName': this.dataForm.attrName,
                'attrValue': this.dataForm.attrValue,
                'attrSort': this.dataForm.attrSort,
                'quickShow': this.dataForm.quickShow
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
