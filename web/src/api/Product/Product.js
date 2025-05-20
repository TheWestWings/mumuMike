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

export function getProductById(id) {
  return request({
    url: `product/${id}`,
    method: 'get',
  });
}

export function updateProduct(data) {
  return request({
    url: 'product',
    method: 'put',
    data
  });
}

export function addProduct(data) {
    return request({
        url: 'product',
        method: 'post',
        data
    });
}