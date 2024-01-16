import {
    fetchOrders
} from '../../../services/order/orderList';

Page({
    page: {
        size: 5,
        num: 1,
    },

    data: {
        enable: false,
        loadingProps: {
            size: '50rpx',
        },
        scrollTop: 0,
        orderList: [],
        emptyImg:
            'https://cdn-we-retail.ym.tencent.com/miniapp/order/empty-order-list.png',
        backRefresh: false,
    },

    onLoad(query) {
        this.init();
    },

    onShow() {
        if (!this.data.backRefresh) return;
        this.onRefresh();
        this.setData({backRefresh: false});
    },

    onReachBottom() {
    },

    onPageScroll(e) {
    },

    init() {
        this.getOrderList();
    },

    getOrderList() {
        this.setData({enable: true});
        return fetchOrders()
            .then((res) => {
                this.setData({
                    orderList: res,
                    enable: false
                })
            });
    },

    onRefresh() {
        this.getOrderList()
    },

    onOrderCardTap(e) {
        const {order} = e.currentTarget.dataset;
        wx.navigateTo({
            url: `/pages/order/order-detail/index?orderId=${order.id}`,
        });
    },
});
