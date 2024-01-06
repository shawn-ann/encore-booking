import {request} from '../_utils/request';

export function sendSms(mobile) {
    let api = "user/send_sms_code/" + mobile
    return request(api, "POST")
}

export function loginSubmit(mobile, smsCode) {
    let api = "user/login"
    return request(api, "POST", {mobile: mobile, smsCode: smsCode})
}