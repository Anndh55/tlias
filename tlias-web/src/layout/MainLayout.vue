<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar" :style="{ width: isCollapse ? '64px' : '220px' }">
      <div class="sidebar-header">
        <span v-show="!isCollapse" class="logo-text">智学云帆</span>
        <span v-show="isCollapse" class="logo-text-mini">云</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :router="true"
        background-color="#1d1e2c"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="content">
          <template #title>
            <el-icon><Management /></el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="/depts">
            <el-icon><Folder /></el-icon>
            <template #title>部门管理</template>
          </el-menu-item>
          <el-menu-item index="/emps">
            <el-icon><User /></el-icon>
            <template #title>员工管理</template>
          </el-menu-item>
          <el-sub-menu index="clazz-student">
            <template #title>
              <el-icon><Reading /></el-icon>
              <span>班级学员管理</span>
            </template>
            <el-menu-item index="/clazzs">
              <el-icon><School /></el-icon>
              <template #title>班级管理</template>
            </el-menu-item>
            <el-menu-item index="/students">
              <el-icon><UserFilled /></el-icon>
              <template #title>学员管理</template>
            </el-menu-item>
          </el-sub-menu>
        </el-sub-menu>

        <el-menu-item index="/system">
          <el-icon><Setting /></el-icon>
          <template #title>系统信息管理</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主区域 -->
    <div class="main-area">
      <!-- 顶部导航 -->
      <div class="topbar">
        <div class="topbar-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="breadcrumb">{{ currentTitle }}</span>
        </div>
        <div class="topbar-right">
          <span class="user-info">
            <el-icon><Avatar /></el-icon>
            {{ user?.name || '未登录' }}
          </span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </div>

      <!-- 内容区 -->
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)

const user = JSON.parse(localStorage.getItem('user') || '{}')

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => route.meta?.title || '首页')

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  background: #1d1e2c;
  transition: width 0.3s;
  overflow-y: auto;
  overflow-x: hidden;
  flex-shrink: 0;
}

.sidebar-header {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  font-weight: 700;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}

.logo-text {
  letter-spacing: 2px;
}
.logo-text-mini {
  font-size: 16px;
}

.sidebar .el-menu {
  border-right: none;
}

.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  height: 56px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  flex-shrink: 0;
}

.topbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}
.collapse-btn:hover {
  color: #409eff;
}

.breadcrumb {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #606266;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f7fa;
}
</style>
