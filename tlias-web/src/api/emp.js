import request from './request'

export function pageEmpAPI(params) {
  return request({ url: '/emps', method: 'get', params })
}

export function listEmpAPI() {
  return request({ url: '/emps/list', method: 'get' })
}

export function getEmpAPI(id) {
  return request({ url: `/emps/${id}`, method: 'get' })
}

export function addEmpAPI(data) {
  return request({ url: '/emps', method: 'post', data })
}

export function updateEmpAPI(data) {
  return request({ url: '/emps', method: 'put', data })
}

export function deleteEmpAPI(ids) {
  return request({ url: '/emps', method: 'delete', params: { ids } })
}
