import request from '@/utils/request'

const prefix = '/booking_order/'

export function fetchList(data) {
  return request({
    url: prefix + 'list',
    method: 'post',
    data
  })
}

export function fetchDetail(id) {
  return request({
    url: prefix + 'detail/' + id,
    method: 'get'
  })
}
