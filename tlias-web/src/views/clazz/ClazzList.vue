<template>
  <div class="page-container">
    <div class="page-header">
      <h2>班级管理</h2>
      <el-button type="primary" @click="openDialog(null)">+ 新增班级</el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-form">
      <el-form :model="query" inline>
        <el-form-item label="班级名称">
          <el-input v-model="query.name" placeholder="请输入班级名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="学科">
          <el-select v-model="query.subject" placeholder="请选择" clearable style="width: 130px">
            <el-option v-for="(v,k) in subjectMap" :key="k" :label="v" :value="k" />
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间">
          <el-date-picker v-model="query.begin" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" style="width: 150px" />
          <span style="margin: 0 8px">至</span>
          <el-date-picker v-model="query.end" type="date" placeholder="结课日期" value-format="YYYY-MM-DD" style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never">
      <el-table :data="clazzList" stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="65" />
        <el-table-column prop="name" label="班级名称" min-width="180" />
        <el-table-column prop="room" label="班级教室" width="100" />
        <el-table-column prop="masterName" label="班主任" width="100" />
        <el-table-column prop="beginDate" label="开课时间" width="120" />
        <el-table-column prop="endDate" label="结课时间" width="120" />
        <el-table-column label="学科" width="80">
          <template #default="{ row }">{{ subjectMap[row.subject] || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="isClassActive(row) ? 'success' : 'info'" size="small">
              {{ isClassActive(row) ? '进行中' : '已结课' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后操作时间" min-width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 16px; display: flex; justify-content: flex-end">
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑班级' : '新增班级'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
            <el-form-item label="班级教室" prop="room">
              <el-input v-model="form.room" placeholder="请输入" />
            </el-form-item>
            <el-form-item label="学科" prop="subject">
              <el-select v-model="form.subject" placeholder="请选择" style="width: 100%">
                <el-option v-for="(v,k) in subjectMap" :key="k" :label="v" :value="k" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班主任" prop="masterId">
              <el-select v-model="form.masterId" placeholder="请选择" style="width: 100%">
                <el-option v-for="e in empList" :key="e.id" :label="e.name" :value="e.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="开课时间" prop="beginDate">
              <el-date-picker v-model="form.beginDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
            <el-form-item label="结课时间" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
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
import { pageClazzAPI, addClazzAPI, updateClazzAPI, deleteClazzAPI, listClazzAPI } from '../../api/clazz.js'
import { listEmpAPI } from '../../api/emp.js'

const subjectMap = { 1: 'Java', 2: '前端', 3: '大数据', 4: 'Python', 5: 'Go', 6: '嵌入式' }

const loading = ref(false)
const saving = ref(false)
const clazzList = ref([])
const total = ref(0)
const empList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const query = reactive({
  name: '', subject: null, begin: null, end: null, page: 1, pageSize: 10
})

const form = ref({
  id: null, name: '', room: '', subject: null, masterId: null, beginDate: null, endDate: null
})

const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'change' }]
}

const isClassActive = (row) => {
  if (!row.endDate) return false
  return new Date(row.endDate) >= new Date()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageClazzAPI({ ...query })
    if (res.code === 1) {
      clazzList.value = res.data.rows || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

const loadEmps = async () => {
  const res = await listEmpAPI()
  if (res.code === 1) empList.value = res.data || []
}

const handleSearch = () => { query.page = 1; fetchData() }
const handleReset = () => { Object.assign(query, { name: '', subject: null, begin: null, end: null, page: 1 }); fetchData() }

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    form.value = { id: row.id, name: row.name, room: row.room, subject: row.subject, masterId: row.masterId, beginDate: row.beginDate, endDate: row.endDate }
  } else {
    isEdit.value = false
    form.value = { id: null, name: '', room: '', subject: null, masterId: null, beginDate: null, endDate: null }
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
      await updateClazzAPI(form.value)
      ElMessage.success('修改成功')
    } else {
      await addClazzAPI(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    await fetchData()
  } finally {
    saving.value = false
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该班级吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await deleteClazzAPI(id)
      ElMessage.success('删除成功')
      await fetchData()
    }).catch(() => {})
}

onMounted(() => { loadEmps(); fetchData() })
</script>

<script>
export default { name: 'ClazzList' }
</script>
