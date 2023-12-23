import request from '@/utils/request'

const prefix = '/inventory/'

export function fetchList(query) {
  return request({
    url: prefix + 'list',
    method: 'get',
    params: query
  })
}

export function createItem(data) {
  return request({
    url: prefix + 'create',
    method: 'post',
    data
  })
}

export function updateQuantity(data) {
  return request({
    url: prefix + 'update_quantity',
    method: 'post',
    data
  })
}

export function deleteItem(id) {
  return request({
    url: prefix + id,
    method: 'delete'
  })
}

export function statusActive(id) {
  return request({
    url: prefix + 'active/' + id,
    method: 'post'
  })
}

export function statusInactive(id) {
  return request({
    url: prefix + 'inactive/' + id,
    method: 'post'
  })
}

export function fetchDropdown(concertId) {
  return request({
    url: prefix + 'dropdown/' + concertId,
    method: 'get'
  })
}
