import {request} from '../_utils/request';

function getMobileFromStorage() {
    let userInfo = wx.getStorageSync('userInfo')
    let mobile = ""
    if (userInfo && userInfo.mobile) {
        mobile = userInfo.mobile
    }
    if (!mobile) {
        return;
    }
    return mobile;
}

export function sendVerifyCode(mobile, isLogin) {
    if (!mobile) {
        mobile = getMobileFromStorage();
    }
    let api = "verify/code/send"
    return request(api, "POST", {mobile: mobile, isLogin: isLogin})
}

export function verifyVerifyCode(mobile, inputCode) {
    if (!mobile) {
        mobile = getMobileFromStorage();
    }
    let api = "verify/code/verify"
    return request(api, "POST", {mobile: mobile, inputCode: inputCode, isLogin: false})
}