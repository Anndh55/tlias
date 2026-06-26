import request from './request'

export function loginAPI(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}
