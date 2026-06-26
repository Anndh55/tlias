import sys, pathlib
p = pathlib.Path(sys.path[0] if sys.path[0] else '.') / 'src' / 'views' / 'dept' / 'DeptList.vue'
extra = '''<script setup>
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
const rules = {
  name: [
    { required: true, message: "\u8bf7\u8f93\u5165\u90e8\u95e8\u540d\u79f0", trigger: "blur" },
    { max: 10, message: "\u540d\u79f0\u4e0d\u80fd\u8d85\u8fc710\u4e2a\u5b57\u7b26", trigger: "blur" }
  ]
}
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listDeptAPI()
    if (res.code === 1) deptList.value = res.data || []
  } finally { loading.value = false }
}
const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    form.value = { id: row.id, name: row.name }
  } else {
    isEdit.value = false
    form.value = { id: null, name: "" }
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
      await updateDeptAPI({ id: form.value.id, name: form.value.name })
      ElMessage.success("\u4fee\u6539\u6210\u529f")
    } else {
      await addDeptAPI({ name: form.value.name })
      ElMessage.success("\u65b0\u589e\u6210\u529f")
    }
    dialogVisible.value = false
    await fetchData()
  } finally { saving.value = false }
}
const handleDelete = (id) => {
  ElMessageBox.confirm("\u786e\u5b9a\u8981\u5220\u9664\u8be5\u90e8\u95e8\u5417\uff1f", "\u63d0\u793a", {
    confirmButtonText: "\u786e\u5b9a", cancelButtonText: "\u53d6\u6d88", type: "warning"
  }).then(async () => {
    await deleteDeptAPI(id)
    ElMessage.success("\u5220\u9664\u6210\u529f")
    await fetchData()
  }).catch(() => {})
}
onMounted(fetchData)
</script>
<style scoped>
</style>
'''
p.write_text(p.read_text(encoding='utf-8') + extra, encoding='utf-8')
print('Done')
