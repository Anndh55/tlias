import request from './request'

export function pageStudentAPI(params) {
  return request({ url: '/students', method: 'get', params })
}

export function listStudentAPI() {
  return request({ url: '/students/list', method: 'get' })
}

export function getStudentAPI(id) {
  return request({ url: `/students/${id}`, method: 'get' })
}

export function addStudentAPI(data) {
  return request({ url: '/students', method: 'post', data })
}

export function updateStudentAPI(data) {
  return request({ url: '/students', method: 'put', data })
}

export function deleteStudentAPI(ids) {
  return request({ url: '/students', method: 'delete', params: { ids } })
}
