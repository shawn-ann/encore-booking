import { config } from '../../config/index';
import {request} from "../_utils/request";

/** 获取商品列表 */
function mockFetchGoodsList(pageIndex = 1, pageSize = 20) {
  const { delay } = require('../_utils/delay');
  const { getGoodsList } = require('../../model/goods');
  return delay().then(() =>
    getGoodsList(pageIndex, pageSize).map((item) => {
      return {
        spuId: item.spuId,
        thumb: item.primaryImage,
        title: item.title,
        price: item.price,
        spuStockQuantity: item.spuStockQuantity,
      };
    }),
  );
}

/** 获取商品列表 */
export function fetchGoodsList(pageIndex = 1, pageSize = 20) {
  // if (config.useMock) {
  //   return mockFetchGoodsList(pageIndex, pageSize);
  // }
  let api = "/wx/ticket/list"
  return request(api, "GET")
}
