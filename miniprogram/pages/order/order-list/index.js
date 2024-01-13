import { OrderStatus } from '../config';
import {
  fetchOrders
} from '../../../services/order/orderList';
import { cosThumb } from '../../../utils/util';

Page({
  page: {
    size: 5,
    num: 1,
  },

  data: {
    tabs: [
      { key: -1, text: '全部' },
      { key: OrderStatus.PENDING_PAYMENT, text: '待付款', info: '' },
      { key: OrderStatus.PENDING_DELIVERY, text: '待发货', info: '' },
      { key: OrderStatus.PENDING_RECEIPT, text: '待收货', info: '' },
      { key: OrderStatus.COMPLETE, text: '已完成', info: '' },
    ],
    curTab: -1,
    orderList: [],
    listLoading: 0,
    pullDownRefreshing: false,
    emptyImg:
      'https://cdn-we-retail.ym.tencent.com/miniapp/order/empty-order-list.png',
    backRefresh: false,
    status: -1,
  },

  onLoad(query) {
    this.init();
    this.pullDownRefresh = this.selectComponent('#wr-pull-down-refresh');
  },

  onShow() {
    // if (!this.data.backRefresh) return;
    // this.onRefresh();
    // this.setData({ backRefresh: false });
  },

  onReachBottom() {
    // if (this.data.listLoading === 0) {
    //   this.getOrderList(this.data.curTab);
    // }
  },

  onPageScroll(e) {
    // this.pullDownRefresh && this.pullDownRefresh.onPageScroll(e);
  },

  onPullDownRefresh_(e) {
    // const { callback } = e.detail;
    // this.setData({ pullDownRefreshing: true });
    // this.refreshList(this.data.curTab)
    //   .then(() => {
    //     this.setData({ pullDownRefreshing: false });
    //     callback && callback();
    //   })
    //   .catch((err) => {
    //     this.setData({ pullDownRefreshing: false });
    //     Promise.reject(err);
    //   });
  },

  init() {
    this.getOrderList();
  },

  getOrderList() {
    this.setData({ listLoading: 1 });
    return fetchOrders()
      .then((res) => {
        this.setData({
          orderList: res,
          listLoading: 0,
        });
      })
      .catch((err) => {
        debugger;
        this.setData({ listLoading: 3 });
        return Promise.reject(err);
      });
  },

  onReTryLoad() {
    this.getOrderList(this.data.curTab);
  },

  onTabChange(e) {
    const { value } = e.detail;
    this.setData({
      status: value,
    });
    this.refreshList(value);
  },

  getOrdersCount() {
    return fetchOrdersCount().then((res) => {
      const tabsCount = res.data || [];
      const { tabs } = this.data;
      tabs.forEach((tab) => {
        const tabCount = tabsCount.find((c) => c.tabType === tab.key);
        if (tabCount) {
          tab.info = tabCount.orderNum;
        }
      });
      this.setData({ tabs });
    });
  },

  refreshList(status = -1) {
    this.page = {
      size: this.page.size,
      num: 1,
    };
    this.setData({ curTab: status, orderList: [] });

    return Promise.all([
      this.getOrderList(status, true),
      this.getOrdersCount(),
    ]);
  },

  onRefresh() {
    this.refreshList(this.data.curTab);
  },

  onOrderCardTap(e) {
    const { order } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/order/order-detail/index?orderId=${order.id}`,
    });
  },
});
