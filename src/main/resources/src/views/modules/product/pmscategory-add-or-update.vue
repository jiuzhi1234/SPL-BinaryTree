<template>
  <el-dialog
    :title="!dataForm.catId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="��������" prop="name">
      <el-input v-model="dataForm.name" placeholder="��������"></el-input>
    </el-form-item>
    <el-form-item label="������id" prop="parentCid">
      <el-input v-model="dataForm.parentCid" placeholder="������id"></el-input>
    </el-form-item>
    <el-form-item label="�㼶" prop="catLevel">
      <el-input v-model="dataForm.catLevel" placeholder="�㼶"></el-input>
    </el-form-item>
    <el-form-item label="�Ƿ���ʾ[0-����ʾ��1��ʾ]" prop="showStatus">
      <el-input v-model="dataForm.showStatus" placeholder="�Ƿ���ʾ[0-����ʾ��1��ʾ]"></el-input>
    </el-form-item>
    <el-form-item label="����" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="����"></el-input>
    </el-form-item>
    <el-form-item label="ͼ���ַ" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="ͼ���ַ"></el-input>
    </el-form-item>
    <el-form-item label="������λ" prop="productUnit">
      <el-input v-model="dataForm.productUnit" placeholder="������λ"></el-input>
    </el-form-item>
    <el-form-item label="��Ʒ����" prop="productCount">
      <el-input v-model="dataForm.productCount" placeholder="��Ʒ����"></el-input>
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
          catId: 0,
          name: '',
          parentCid: '',
          catLevel: '',
          showStatus: '',
          sort: '',
          icon: '',
          productUnit: '',
          productCount: ''
        },
        dataRule: {
          name: [
            { required: true, message: '��������不能为空', trigger: 'blur' }
          ],
          parentCid: [
            { required: true, message: '������id不能为空', trigger: 'blur' }
          ],
          catLevel: [
            { required: true, message: '�㼶不能为空', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: '�Ƿ���ʾ[0-����ʾ��1��ʾ]不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '����不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: 'ͼ���ַ不能为空', trigger: 'blur' }
          ],
          productUnit: [
            { required: true, message: '������λ不能为空', trigger: 'blur' }
          ],
          productCount: [
            { required: true, message: '��Ʒ����不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.catId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.catId) {
            this.$http({
              url: this.$http.adornUrl(`/product/pmscategory/info/${this.dataForm.catId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.pmsCategory.name
                this.dataForm.parentCid = data.pmsCategory.parentCid
                this.dataForm.catLevel = data.pmsCategory.catLevel
                this.dataForm.showStatus = data.pmsCategory.showStatus
                this.dataForm.sort = data.pmsCategory.sort
                this.dataForm.icon = data.pmsCategory.icon
                this.dataForm.productUnit = data.pmsCategory.productUnit
                this.dataForm.productCount = data.pmsCategory.productCount
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
              url: this.$http.adornUrl(`/product/pmscategory/${!this.dataForm.catId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'catId': this.dataForm.catId || undefined,
                'name': this.dataForm.name,
                'parentCid': this.dataForm.parentCid,
                'catLevel': this.dataForm.catLevel,
                'showStatus': this.dataForm.showStatus,
                'sort': this.dataForm.sort,
                'icon': this.dataForm.icon,
                'productUnit': this.dataForm.productUnit,
                'productCount': this.dataForm.productCount
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
