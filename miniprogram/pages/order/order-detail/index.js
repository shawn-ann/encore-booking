import {
    fetchOrderDetail,
} from '../../../services/order/orderDetail';

Page({
    data: {
        enable: false,
        loadingProps: {
            size: '50rpx',
        },
        scrollTop: 0,
        orderId: undefined,
        order: {}, // 后台返回的原始数据
        countDownTime: null,
        backRefresh: false, // 用于接收其他页面back时的状态
    },

    onLoad(query) {
        this.setData({
            orderId: query.orderId,
        });
        this.init();
    },

    onShow() {
        // 当从其他页面返回，并且 backRefresh 被置为 true 时，刷新数据
        if (!this.data.backRefresh) return;
        this.onRefresh();
        this.setData({backRefresh: false});
    },

    onPageScroll(e) {
    },

    onImgError(e) {
        if (e.detail) {
            console.error('img 加载失败');
        }
    },

    // 页面初始化，会展示pageLoading
    init() {
        this.getDetail();
    },

    // 页面刷新，展示下拉刷新
    onRefresh() {
        this.init();
        // 如果上一页为订单列表，通知其刷新数据
        const pages = getCurrentPages();
        const lastPage = pages[pages.length - 2];
        if (lastPage) {
            lastPage.data.backRefresh = true;
        }
    },

    getDetail() {
        this.setData({enable: true});
        return fetchOrderDetail(this.data.orderId).then((res) => {
            let order = res;
            this.setData({
                order: order,
                countDownTime: this.computeCountDownTime(order),
                isPaid: order.status == 'PAID',
                enable: false
            });
        });
    },


    // 仅对待支付状态计算付款倒计时
    // 返回时间若是大于2020.01.01，说明返回的是关闭时间，否则说明返回的直接就是剩余时间
    computeCountDownTime(order) {
        if (order.status !== 'CREATED') return null;
        return order.autoCancelTime > 1577808000000
            ? order.autoCancelTime - Date.now()
            : order.autoCancelTime;
    },

    onCountDownFinish() {
        //this.setData({ countDownTime: -1 });
        const {countDownTime, order} = this.data;
        if (
            countDownTime > 0 ||
            (order && order.residueTime > 0)
        ) {
            this.onRefresh();
        }
    },

});
