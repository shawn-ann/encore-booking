import {request} from "../_utils/request";

/** 获取商品列表 */
export function fetchTickets(concertId) {
    let api = "tickets/" + concertId
    return request(api, "GET")
}
