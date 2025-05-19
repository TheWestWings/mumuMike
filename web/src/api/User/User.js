import request from '@/utils/request'



export function getUserInfoById(id) {
  return request({
    url: '/getUserInfo1ById',
    method: 'get',
    params: { id },
  })
}

export function getUserList() {
  return request({
    url: '/getList',
    method: 'get',
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/updateUser',
    method: 'put',
    data,
  })
}

export function updateStatus(data) {
  return request({
    url: '/updateStatus',
    method: 'put',
    data,
  })
}


export function updateRole(data) {
  return request({
    url: '/updateRole',
    method: 'put',
    data,
  })
}
