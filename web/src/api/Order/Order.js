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
    method: 'get',
    
  })
}

export function updateOrderProductStatus(data) {
  return request({
    url: '/order/updateOrderProduct',
    method: 'put',
    data,
  })
}