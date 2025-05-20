import request from '@/utils/request'

export function getMessageList(id) {
  return request({
    url: `/message/getMessageListByUserId/${id}`,
    method: 'get',
    
  })
}