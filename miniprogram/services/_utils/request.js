import {serverUrl} from '../../config/index';

export function request(api, method, data = "") {
    return new Promise((resolve, reject) => {
        wx.request({
            url: serverUrl + api, // 实际API的请求地址
            method: 'POST', // 请求方法，根据实际情况修改
            data: data,
            success: (res) => {
                if (res.data.code == 20000) {
                    resolve(res.data.data); // 返回请求成功的数据
                } else if (res.data.code == 50001) {
                    // 未登录
                    wx.redirectTo({
                        url: 'pages/login/index'
                    })
                } else {
                    wx.showToast({
                        title: res.data.message,
                        icon: 'error',
                        duration: 2000
                    })
                }
            },
            fail: (error) => {
                reject(error); // 返回请求失败的错误信息
            }
        });
    });
}
