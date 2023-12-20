import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/agents',
    method: 'get',
    params
  })
}
