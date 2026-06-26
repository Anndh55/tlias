<template>
  <div class="page-container">
    <div class="page-header">
      <h2>学员管理</h2>
      <el-button type="primary" @click="openDialog(null)">+ 新增学员</el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-form">
      <el-form :model="query" inline>
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="请输入姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="query.gender" placeholder="请选择" clearable style="width: 110px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="最高学历">
          <el-select v-model="query.degree" placeholder="请选择" clearable style="width: 120px">
            <el-option v-for="(v,k) in degreeMap" :key="k" :label="v" :value="k" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="query.clazzId" placeholder="请选择班级" clearable filterable style="width: 200px">
            <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never">
      <el-table :data="studentList" stripe v-loading="loading">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="姓名" width="90" />
        <el-table-column prop="no" label="学号" width="120" />
        <el-table-column prop="clazzName" label="班级" min-width="160" />
        <el-table-column label="性别" width="55">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="学历" width="80">
          <template #default="{ row }">{{ degreeMap[row.degree] || '-' }}</template>
        </el-table-column>
        <el-table-column label="院校" width="65">
          <template #default="{ row }">{{ row.isCollege === 1 ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column prop="violationCount" label="违纪次数" width="85" align="center" />
        <el-table-column prop="violationScore" label="违纪扣分" width="85" align="center" />
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑学员' : '新增学员'" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
            <el-form-item label="学号" prop="no">
              <el-input v-model="form.no" :disabled="isEdit" placeholder="请输入" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入" maxlength="11" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入" maxlength="18" />
            </el-form-item>
            <el-form-item label="所属班级" prop="clazzId">
              <el-select v-model="form.clazzId" placeholder="请选择" filterable style="width: 100%">
                <el-option v-for="c in allClazz" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高学历" prop="degree">
              <el-select v-model="form.degree" placeholder="请选择" style="width: 100%">
                <el-option v-for="(v,k) in degreeMap" :key="k" :label="v" :value="k" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否院校" prop="isCollege">
              <el-radio-group v-model="form.isCollege">
                <el-radio :value="1">是</el-radio>
                <el-radio :value="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="毕业时间">
              <el-date-picker v-model="form.graduationDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
            <el-form-item label="联系地址">
              <el-input v-model="form.address" placeholder="请输入" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item label="违纪次数">
              <el-input-number v-model="form.violationCount" :min="0" style="width: 100%" />
            </el-form-item>
            <el-form-item label="违纪扣分">
              <el-input-number v-model="form.violationScore" :min="0" style="width: 100%" />
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
import { pageStudentAPI, addStudentAPI, updateStudentAPI, deleteStudentAPI } from '../../api/student.js'
import { listClazzAPI } from '../../api/clazz.js'

const degreeMap = { 1: '初中', 2: '高中', 3: '大专', 4: '本科', 5: '硕士', 6: '博士' }

const loading = ref(false)
const saving = ref(false)
const studentList = ref([])
const total = ref(0)
const allClazz = ref([])
const clazzList = ref([]) // for search dropdown
const selectedIds = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const query = reactive({
  name: '', gender: null, degree: null, clazzId: null, page: 1, pageSize: 10
})

const form = ref({
  id: null, name: '', no: '', gender: 1, phone: '', idCard: '',
  isCollege: 0, address: '', degree: null, graduationDate: null,
  clazzId: null, violationCount: 0, violationScore: 0
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '格式不正确', trigger: 'blur' }
  ],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageStudentAPI({ ...query })
    if (res.code === 1) {
      studentList.value = res.data.rows || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

const loadClazz = async () => {
  const res = await listClazzAPI()
  if (res.code === 1) {
    allClazz.value = res.data || []
    clazzList.value = res.data || []
  }
}

const handleSearch = () => { query.page = 1; fetchData() }
const handleReset = () => {
  Object.assign(query, { name: '', gender: null, degree: null, clazzId: null, page: 1 })
  fetchData()
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    form.value = { ...row }
  } else {
    isEdit.value = false
    form.value = {
      id: null, name: '', no: '', gender: 1, phone: '', idCard: '',
      isCollege: 0, address: '', degree: null, graduationDate: null,
      clazzId: null, violationCount: 0, violationScore: 0
    }
  }
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      await updateStudentAPI(form.value)
      ElMessage.success('修改成功')
    } else {
      await addStudentAPI(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    await fetchData()
  } finally {
    saving.value = false
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该学员吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await deleteStudentAPI(id)
      ElMessage.success('删除成功')
      await fetchData()
    }).catch(() => {})
}

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 名学员吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await deleteStudentAPI(selectedIds.value.join(','))
      ElMessage.success('删除成功')
      selectedIds.value = []
      await fetchData()
    }).catch(() => {})
}

onMounted(() => { loadClazz(); fetchData() })
</script>

<script>
export default { name: 'StudentList' }
</script>
