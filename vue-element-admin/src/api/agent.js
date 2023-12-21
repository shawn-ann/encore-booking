import request from '@/utils/request'

const prefix = '/agent/'
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

export function updateItem(data) {
  return request({
    url: prefix + 'update',
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
