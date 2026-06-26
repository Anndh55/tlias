import request from './request'

export function listDeptAPI() {
  return request({ url: '/depts', method: 'get' })
}

export function getDeptAPI(id) {
  return request({ url: `/depts/${id}`, method: 'get' })
}

export function addDeptAPI(data) {
  return request({ url: '/depts', method: 'post', data })
}

export function updateDeptAPI(data) {
  return request({ url: '/depts', method: 'put', data })
}

export function deleteDeptAPI(id) {
  return request({ url: `/depts/${id}`, method: 'delete' })
}
