import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/agent/list',
    method: 'get',
    params: query
  })
}

export function fetch(id) {
  return request({
    url: '/agent/' + id,
    method: 'get'
  })
}

export function createAgent(data) {
  return request({
    url: '/agent/create',
    method: 'post',
    data
  })
}

export function updateAgent(data) {
  return request({
    url: '/agent/update',
    method: 'post',
    data
  })
}
export function deleteAgent(id) {
  return request({
    url: '/agent/' + id,
    method: 'delete'
  })
}
