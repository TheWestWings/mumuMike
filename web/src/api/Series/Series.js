import request from '@/utils/request';

export function updateStatus(data) {
  return request({
    url: '/productType/updateProductTypeStatus',
    method: 'put',
    data
  });
}

export function getSeriesList() {
  return request({
    url: '/productType/getProductTypeList',
    method: 'post',
  });
}
export function addSeries(data) {
  return request({
    url: '/productType',
    method: 'post',
    data
  });
}

export function updateSeries(data) {
  return request({
    url: '/productType',
    method: 'put',
    data
  });
}

export function getSeriesById(id) {
  return request({
    url: `/productType/${id}`,
    method: 'get'
  });
}

export function deleteSeries(id) {
  return request({
    url: `/productType/${id}`,
    method: 'delete'
  });
}