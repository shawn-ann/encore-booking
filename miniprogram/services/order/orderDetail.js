import {config} from '../../config/index';
import {request} from "../_utils/request";

/** 获取订单详情数据 */
export function fetchOrderDetail(orderId) {
    let api = "order/detail/" + orderId
    return request(api, "GET")
}

export function cancelOrder(orderId) {
    let api = "order/cancel/" + orderId
    return request(api, "POST",)
}

export function applyRefund(orderId) {
    let api = "order/refund/" + orderId
    return request(api, "POST",)
}