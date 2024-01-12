import {request} from '../_utils/request';

export function loginSubmit(mobile, smsCode) {
    let api = "user/login"
    return request(api, "POST", {mobile: mobile, smsCode: smsCode})
}