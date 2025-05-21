import request from '@/utils/request'

export function getMessageList(id) {
    return request({
        url: `/message/getMessageListByUserId/${id}`,
        method: 'get',

    })
}

export function updateMessageStatus(data) {
    return request({
        url: '/message',
        method: 'put',
        data,
    })
}