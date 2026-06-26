<template>
  <div class="page-container">
    <h2 style="margin-bottom: 20px">系统概览</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6f7ff; color: #1890ff">
              <el-icon :size="28"><Folder /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ stats.deptCount }}</span>
              <span class="stat-label">部门数</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f0f5ff; color: #2f54eb">
              <el-icon :size="28"><User /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ stats.empCount }}</span>
              <span class="stat-label">员工数</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f6ffed; color: #52c41a">
              <el-icon :size="28"><School /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ stats.clazzCount }}</span>
              <span class="stat-label">班级数</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #fff7e6; color: #fa8c16">
              <el-icon :size="28"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ stats.studentCount }}</span>
              <span class="stat-label">学员数</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>快速入口</span>
          </template>
          <div style="display: flex; gap: 16px; flex-wrap: wrap">
            <el-button type="primary" @click="$router.push('/depts')">部门管理</el-button>
            <el-button type="success" @click="$router.push('/emps')">员工管理</el-button>
            <el-button type="warning" @click="$router.push('/clazzs')">班级管理</el-button>
            <el-button type="info" @click="$router.push('/students')">学员管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { listDeptAPI } from '../api/dept.js'
import { listEmpAPI } from '../api/emp.js'
import { listClazzAPI } from '../api/clazz.js'
import { listStudentAPI } from '../api/student.js'

const stats = reactive({
  deptCount: 0,
  empCount: 0,
  clazzCount: 0,
  studentCount: 0
})

onMounted(async () => {
  try {
    const [deptRes, empRes, clazzRes, stuRes] = await Promise.all([
      listDeptAPI(), listEmpAPI(), listClazzAPI(), listStudentAPI()
    ])
    if (deptRes.code === 1) stats.deptCount = deptRes.data?.length || 0
    if (empRes.code === 1) stats.empCount = empRes.data?.length || 0
    if (clazzRes.code === 1) stats.clazzCount = clazzRes.data?.length || 0
    if (stuRes.code === 1) stats.studentCount = stuRes.data?.length || 0
  } catch (e) {
    // ignore
  }
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
</style>
