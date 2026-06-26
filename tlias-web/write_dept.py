import sys, os
os.chdir(os.path.dirname(os.path.abspath(__file__)))
p = os.path.join('src', 'views', 'dept', 'DeptList.vue')
content = open(p, 'r', encoding='utf-8').read()
content += '''<script setup>
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
'''
open(p, 'w', encoding='utf-8').write(content)
print('DeptList.vue restored OK')
