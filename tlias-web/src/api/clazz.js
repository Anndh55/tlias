import request from './request'

export function pageClazzAPI(params) {
  return request({ url: '/clazzs', method: 'get', params })
}

export function listClazzAPI() {
  return request({ url: '/clazzs/list', method: 'get' })
}

export function getClazzAPI(id) {
  return request({ url: `/clazzs/${id}`, method: 'get' })
}

export function addClazzAPI(data) {
  return request({ url: '/clazzs', method: 'post', data })
}

export function updateClazzAPI(data) {
  return request({ url: '/clazzs', method: 'put', data })
}

export function deleteClazzAPI(id) {
  return request({ url: `/clazzs/${id}`, method: 'delete' })
}
