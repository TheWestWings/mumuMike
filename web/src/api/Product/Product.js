import request from '@/utils/request';

export function getProductList() {
  return request({
    url: 'product/getProductList',
    method: 'post',
  });
}