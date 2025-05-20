import request from '@/utils/request'

export function submitOrder(data) {
  return request({
    url: '/order',
    method: 'post',
    data,
  })
}

export function getOrderList() {
  return request({
    url: '/order/getListVO',
    method: 'post',
    data: {status: 0},
  })
}

export function updateOrderProductStatus(data) {
  return request({
    url: '/order/updateOrderProduct',
    method: 'put',
    data,
  })
}

export function getHistoryOrderList() {
  return request({
    url: '/order/getListVO',
    method: 'post',
    data: {status: 2},
  })
}

export function updateOrderStatus(data) {
  return request({
    url: '/order/updateOrderStatus',
    method: 'put',
    data,
  })
}
