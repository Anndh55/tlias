<template>
<div class="page-container">
<div class="page-header">
<h2>部门管理</h2>
<el-button type="primary" @click="openDialog(null)">+ 新增部门</el-button>
</div>
<el-card shadow="never">
<el-table :data="deptList" stripe v-loading="loading">
<el-table-column type="index" label="序号" width="80" />
<el-table-column prop="name" label="部门名称" min-width="180" />
<el-table-column prop="updateTime" label="最后操作时间" min-width="200" />
<el-table-column label="操作" width="160" fixed="right">
<template #default="{ row }">
<el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
<el-button type="danger" link size="small" @click="handleDelete(row.id)">删除</el-button>
</template>
</el-table-column>
</el-table>
</el-card>
<el-dialog v-model="dialogVisible" :title="isEdit ? '编辑部门' : '新增部门'" width="420px">
<el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
<el-form-item label="部门名称" prop="name">
<el-input v-model="form.name" placeholder="请输入部门名称" maxlength="10" />
</el-form-item>
</el-form>
<template #footer>
<el-button @click="dialogVisible = false">取消</el-button>
<el-button type="primary" :loading="saving" @click="handleSave">确认</el-button>
</template>
</el-dialog>
</div>
</template>
<script setup>
import { ref, onMounted, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { listDeptAPI, addDeptAPI, updateDeptAPI, deleteDeptAPI } from "../../api/dept.js"
const loading = ref(false)
const saving = ref(false)
const deptList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = ref({ id: null, name: "" })
const rules = { name: [ { required: true, message: "请输多部门名称", trigger: "blur" }, { max: 10, message: "名称不能超过10个字符", trigger: "blur" } ] }
const fetchData = async () => { loading.value = true; try { const res = await listDeptAPI(); if (res.code === 1) deptList.value = res.data || [] } finally { loading.value = false } }
const openDialog = (row) => { if (row) { isEdit.value = true; form.value = { id: row.id, name: row.name } } else { isEdit.value = false; form.value = { id: null, name: "" } }; dialogVisible.value = true; nextTick(() => formRef.value?.clearValidate()) }
const handleSave = async () => { const valid = await formRef.value.validate().catch(() => false); if (!valid) return; saving.value = true; try { if (isEdit.value) { await updateDeptAPI({ id: form.value.id, name: form.value.name }); ElMessage.success("修改成功") } else { await addDeptAPI({ name: form.value.name }); ElMessage.success("新增成功") }; dialogVisible.value = false; await fetchData() } finally { saving.value = false } }
const handleDelete = (id) => { ElMessageBox.confirm("确定要删除该部门吗？", "提示", { confirmButtonText: "确定", cancelButtonText: "取消", type: "warning" }).then(async () => { await deleteDeptAPI(id); ElMessage.success("删除成功"); await fetchData() }).catch(() => {}) }
onMounted(fetchData)
</script>
<style scoped></style>
