<template>
  <div class="page-container">
    <div class="page-header">
      <h2>员工管理</h2>
      <el-button type="primary" @click="openDialog(null)">+ 新增员工</el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-form">
      <el-form :model="query" inline>
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="请输入姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="query.gender" placeholder="请选择" clearable style="width: 120px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="query.begin" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" style="width: 150px" />
          <span style="margin: 0 8px">至</span>
          <el-date-picker v-model="query.end" type="date" placeholder="结束日期" value-format="YYYY-MM-DD" style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 员工表格 -->
    <el-card shadow="never">
      <el-table :data="empList" stripe v-loading="loading">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="60">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column label="头像" width="70">
          <template #default="{ row }">
            <el-avatar :src="row.image" shape="square" :size="36" />
          </template>
        </el-table-column>
        <el-table-column prop="deptName" label="所属部门" width="120" />
        <el-table-column label="职位" width="100">
          <template #default="{ row }">{{ positionMap[row.position] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="hireDate" label="入职日期" width="120" />
        <el-table-column prop="updateTime" label="最后操作时间" min-width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 16px; display: flex; justify-content: space-between; align-items: center">
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          批量删除
        </el-button>
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑员工' : '新增员工'" width="720px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" :disabled="isEdit" placeholder="登录用户名" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
            <el-form-item label="职位" prop="position">
              <el-select v-model="form.position" placeholder="请选择" style="width: 100%">
                <el-option v-for="(v,k) in positionMap" :key="k" :label="v" :value="k" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择" style="width: 100%">
                <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="薪资" prop="salary">
              <el-input-number v-model="form.salary" :min="0" style="width: 100%" />
            </el-form-item>
            <el-form-item label="入职日期" prop="hireDate">
              <el-date-picker v-model="form.hireDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
            <el-form-item label="头像">
              <el-upload
                class="avatar-upload"
                :show-file-list="false"
                :http-request="handleUpload"
              >
                <el-avatar v-if="form.image" :src="form.image" :size="80" shape="square" />
                <el-icon v-else :size="40"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageEmpAPI, addEmpAPI, updateEmpAPI, deleteEmpAPI } from '../../api/emp.js'
import { listDeptAPI } from '../../api/dept.js'
import { uploadAPI } from '../../api/upload.js'

const positionMap = { 1: '班主任', 2: '讲师', 3: '学工主管', 4: '教研主管', 5: '咨询师' }

const loading = ref(false)
const saving = ref(false)
const empList = ref([])
const total = ref(0)
const deptList = ref([])
const selectedIds = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const query = reactive({
  name: '',
  gender: null,
  begin: null,
  end: null,
  page: 1,
  pageSize: 10
})

const form = ref({
  id: null, username: '', password: '123456', name: '', gender: 1,
  phone: '', position: null, salary: null, image: '', hireDate: null, deptId: null
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageEmpAPI({ ...query })
    if (res.code === 1) {
      empList.value = res.data.rows || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

const loadDepts = async () => {
  const res = await listDeptAPI()
  if (res.code === 1) deptList.value = res.data || []
}

const handleSearch = () => { query.page = 1; fetchData() }
const handleReset = () => { Object.assign(query, { name: '', gender: null, begin: null, end: null, page: 1 }); fetchData() }

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    form.value = { ...row, password: '123456' }
  } else {
    isEdit.value = false
    form.value = { id: null, username: '', password: '123456', name: '', gender: 1, phone: '', position: null, salary: null, image: '', hireDate: null, deptId: null }
  }
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const handleUpload = async (options) => {
  try {
    const res = await uploadAPI(options.file)
    if (res.code === 1) {
      form.value.image = res.data
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      await updateEmpAPI(form.value)
      ElMessage.success('修改成功')
    } else {
      await addEmpAPI(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    await fetchData()
  } finally {
    saving.value = false
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该员工吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await deleteEmpAPI(id)
      ElMessage.success('删除成功')
      await fetchData()
    }).catch(() => {})
}

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 名员工吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await deleteEmpAPI(selectedIds.value.join(','))
      ElMessage.success('删除成功')
      selectedIds.value = []
      await fetchData()
    }).catch(() => {})
}

onMounted(() => { loadDepts(); fetchData() })
</script>

<script>
export default { name: 'EmpList' }
</script>

<style scoped>
.avatar-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}
.avatar-upload:hover {
  border-color: #409eff;
}
</style>
