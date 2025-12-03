const BASE_URL = 'http://localhost:8080'; // Change to your actual backend IP if testing on device

const request = (options) => {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token');
        uni.request({
            url: BASE_URL + (options.url.startsWith('/') ? options.url : '/' + options.url),
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Authorization': token ? `Bearer ${token}` : '',
                'Content-Type': 'application/json',
                ...options.header
            },
            success: (res) => {
                if (res.statusCode === 200) {
                    if (res.data.code === 401 || res.data.code === 403) {
                        uni.removeStorageSync('token');
                        uni.navigateTo({
                            url: '/pages/login/login'
                        });
                        reject(res.data);
                    } else {
                        resolve(res.data);
                    }
                } else {
                    uni.showToast({
                        title: '网络请求失败',
                        icon: 'none'
                    });
                    reject(res);
                }
            },
            fail: (err) => {
                uni.showToast({
                    title: '网络请求错误',
                    icon: 'none'
                });
                reject(err);
            }
        });
    });
};

export default request;
