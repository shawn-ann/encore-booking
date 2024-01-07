import {request} from "../_utils/request";

/** 获取商品列表 */
export function fetchConcerts() {
    let api = "concerts"
    return request(api, "GET")
}
