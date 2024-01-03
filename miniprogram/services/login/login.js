import {config, cdnBase} from '../../config/index';

export function sendSms(mobile) {
    const {request} = require('../_utils/request');
    return new Promise((resolve, reject) => {
        wx.request({
            url: 'http://localhost:9999/wx/user/send_sms_code/' + mobile, // 实际API的请求地址
            method: 'POST', // 请求方法，根据实际情况修改
            success: (res) => {
                resolve(res.data); // 返回请求成功的数据
            },
            fail: (error) => {
                reject(error); // 返回请求失败的错误信息
            }
        });
    });
}
