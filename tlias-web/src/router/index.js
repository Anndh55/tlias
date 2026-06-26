import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'depts',
        name: 'Dept',
        component: () => import('../views/dept/DeptList.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'emps',
        name: 'Emp',
        component: () => import('../views/emp/EmpList.vue'),
        meta: { title: '员工管理' }
      },
      {
        path: 'clazzs',
        name: 'Clazz',
        component: () => import('../views/clazz/ClazzList.vue'),
        meta: { title: '班级管理' }
      },
      {
        path: 'students',
        name: 'Student',
        component: () => import('../views/student/StudentList.vue'),
        meta: { title: '学员管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫 - 未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
