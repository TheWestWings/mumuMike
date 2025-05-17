import request from '@/utils/request'

export function getSeriesList() {
  return request({
    url: '/product/getProductVOList',
    method: 'get',
  })
}