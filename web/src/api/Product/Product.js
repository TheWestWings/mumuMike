import request from '@/utils/request';

export function getProductList() {
  return request({
    url: 'product/getProductList',
    method: 'post',
  });
}

export function updateProductStatus(data) {
  return request({
    url: 'product/updateProductStatus',
    method: 'put',
    data
  });
}