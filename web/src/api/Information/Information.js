import request from '@/utils/request'

export function getInformationList(data){
    return request({
        url: '/information/getList',
        method: 'post',
        data
    })
}

export function getInformationVOList(data){
    return request({
        url: '/information/getVOList',
        method: 'post',
        data
    })
}

export function getInformationById(id){
    return request({
        url: '/information/' + id,
        method: 'get',
    })
}

export function addInformation(data){
    return request({
        url: '/information',
        method: 'post',
        data
    })
}

export function updateInformation(data){
    return request({
        url: '/information',
        method: 'put',
        data
    })
}