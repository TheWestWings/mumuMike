import request from '@/utils/request';

export const login = (data) => request({ url: '/login', method: 'POST', data });
export const register = (data) => request({ url: '/register', method: 'POST', data });
export const getProductList = (data) => request({ url: '/product/getProductList', method: 'POST', data });
export const getProductVOList = () => request({ url: '/product/getProductVOList', method: 'GET' }); // Assuming this exists based on web code
export const getProductById = (id) => request({ url: `/product/${id}`, method: 'GET' });
export const createOrder = (data) => request({ url: '/order', method: 'POST', data });
export const getOrderList = (data) => request({ url: '/order/getListVO', method: 'POST', data });
export const getOrderById = (id) => request({ url: `/order/getOrderVOById/${id}`, method: 'GET' });
export const createFeedback = (data) => request({ url: '/information', method: 'POST', data });
