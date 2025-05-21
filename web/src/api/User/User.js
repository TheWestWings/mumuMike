import request from '@/utils/request'



export function getUserInfoById(id) {
  return request({
    url: '/getUserInfo1ById',
    method: 'get',
    params: { id },
  })
}

export function getUserList(data) {
  return request({
    url: '/getList',
    method: 'post',
    data
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

export function getInfo(data){
    return request({
        url: '/getInfo',
        method: 'get',
        data
    })
}

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data,
  })
}

export function register(data){
    return request({
        url: '/register',
        method: 'post',
        data
    })
}

